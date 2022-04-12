package pl.kossa.myflights.fragments.login

import android.util.Log
import android.util.Patterns
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import pl.kossa.myflights.R
import pl.kossa.myflights.api.services.UserService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userService: UserService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val _email = MutableStateFlow("")
    private val _password = MutableStateFlow("")

    val isLoginButtonEnabled = combine(_email, _password) { email, password ->
        return@combine password.isNotBlank() && email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(
            email
        ).matches()
    }
    val emailError = MutableStateFlow<Int?>(null)
    val passwordError = MutableStateFlow<Int?>(null)

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun navigateToCreateAccount() {
        navigate(LoginFragmentDirections.goToCreateAccount())
    }

    internal fun login() {
        emailError.value = null
        passwordError.value = null
        isLoadingData.value = true
        when {
            _email.value.isBlank() -> {
                emailError.value = R.string.empty_email
                isLoadingData.value = false
            }
            _password.value.isBlank() -> {
                passwordError.value = R.string.empty_password
                isLoadingData.value = false
            }
            else -> {
                firebaseAuth.signInWithEmailAndPassword(_email.value, _password.value)
                    .addOnSuccessListener {
                        if (firebaseAuth.currentUser != null && firebaseAuth.currentUser?.isEmailVerified == true) {
                            refreshToken {
                                Log.d("MyLog", "Token Login: $it")
                                analyticsTracker.setUserId(firebaseAuth.currentUser?.uid)
                                fcmHandler.enableFCM {
                                    fcmHandler.refreshFCMToken()
                                    navigate(LoginFragmentDirections.goToMainActivity(), true)
                                    isLoadingData.value = false
                                }
                            }
                        } else {
                            resendVerificationEmail()
                        }
                    }.addOnFailureListener {
                        when (it) {
                            is FirebaseAuthInvalidUserException -> {
                                emailError.value = R.string.no_user_error
                                _password.value = ""
                            }
                            is FirebaseAuthInvalidCredentialsException -> {
                                when {
                                    it.message?.contains("email") == true -> {
                                        emailError.value = R.string.not_email_error
                                    }
                                    it.message?.contains("password") == true -> {
                                        passwordError.value = R.string.wrong_password_error
                                    }
                                    else -> {
                                        Log.d("MyLog", "Login exception$it")
                                        setToastMessage(R.string.unexpected_error)
                                    }
                                }
                            }
                            is FirebaseNetworkException -> {
                                setToastMessage(R.string.error_no_internet)
                            }
                            else -> {
                                Log.d("MyLog", "Login exception$it")
                                setToastMessage(R.string.unexpected_error)
                            }

                        }
                        isLoadingData.value = false
                    }
            }
        }
    }

    private fun resendVerificationEmail() {
        firebaseAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
            navigateToResendEmail()
        }?.addOnFailureListener {
            when (it) {
                is FirebaseNetworkException -> setToastMessage(R.string.error_no_internet)
                else -> setToastMessage(R.string.unexpected_error)
            }
        }
    }

    private fun navigateToResendEmail() {
        navigate(LoginFragmentDirections.goToEmailResend())
    }

    fun signInWithCredential(credential: AuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                val isNew = it.additionalUserInfo?.isNewUser == true
                refreshToken {
                    analyticsTracker.setUserId(firebaseAuth.currentUser?.uid)
                    fcmHandler.enableFCM {
                        fcmHandler.refreshFCMToken()
                    }
                    makeRequest {
                        val response = userService.getUser()
                        val user = response.body!!
                        Log.d("MyLog", "New User: $user")
                        if (!user.regulationsAccepted || isNew) {
                            navigate(LoginFragmentDirections.goToFillProfile())
                        } else {
                            navigate(LoginFragmentDirections.goToMainActivity(), true)
                        }
                    }
                }
            }
            .addOnFailureListener {
                setToastMessage(R.string.error_sign_in_with_google)
                //TODO handle errors
                throw it
            }
    }
}