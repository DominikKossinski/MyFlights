package pl.kossa.myflights.fragments.login

import android.util.Log
import android.util.Patterns
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import okhttp3.ResponseBody
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.ApiErrorBody
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import retrofit2.Converter
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
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
        navDirectionLiveData.value = LoginFragmentDirections.goToCreateAccount()
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
                        if (firebaseAuth.currentUser != null && firebaseAuth.currentUser?.isEmailVerified ?: false) {
                            refreshToken {
                                Log.d("MyLog", "Token Login: $it")
                                navDirectionLiveData.value =
                                    LoginFragmentDirections.goToMainActivity()
                                isLoadingData.value = false
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
        navDirectionLiveData.value = LoginFragmentDirections.goToEmailResend()
    }
}