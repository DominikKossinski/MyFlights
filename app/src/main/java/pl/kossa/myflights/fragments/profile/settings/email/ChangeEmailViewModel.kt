package pl.kossa.myflights.fragments.profile.settings.email

import android.util.Log
import android.util.Patterns
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.User
import pl.kossa.myflights.api.services.UserService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.repository.UserRepository
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class ChangeEmailViewModel @Inject constructor(
    private val userRepository: UserRepository,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    val user = MutableStateFlow<User?>(null)
    private val _password = MutableStateFlow("")
    val passwordError = MutableStateFlow<Int?>(null)
    private val _newEmail = MutableStateFlow("")
    val newEmailError = MutableStateFlow<Int?>(null)
    private val _confirmNewEmail = MutableStateFlow("")

    val isSaveButtonEnabled =
        combine(
            _password,
            _newEmail,
            _confirmNewEmail,
            user
        ) { password, newEmail, confirmEmail, user ->
            return@combine password.isNotBlank() && newEmail.isNotBlank() &&
                    Patterns.EMAIL_ADDRESS.matcher(newEmail).matches() &&
                    newEmail.contentEquals(confirmEmail) && user != null
        }

    init {
        fetchUser()
    }

    private fun fetchUser() {
        makeRequest {
            val response = handleRequest {
                userRepository.getUser()
            }
            user.value = response
        }
    }

    fun changeEmail() {
        isLoadingData.value = true
        passwordError.value = null
        newEmailError.value = null
        val email = user.value?.email
        if (email == null) {
            isLoadingData.value = false
            return
        }
        val credential = EmailAuthProvider.getCredential(email, _password.value)
        firebaseAuth.currentUser?.reauthenticate(credential)
            ?.addOnSuccessListener {
                firebaseAuth.currentUser?.updateEmail(_newEmail.value)
                    ?.addOnSuccessListener {
                        firebaseAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                            isLoadingData.value = false
                            navigateToEmailVerify()
                        }?.addOnFailureListener {
                            isLoadingData.value = false
                            when (it) {
                                is FirebaseNetworkException -> {
                                    setToastMessage(R.string.error_no_internet)
                                    navigateBack()
                                }
                                else -> {
                                    setToastMessage(R.string.unexpected_error)
                                }
                            }
                        }
                    }
                    ?.addOnFailureListener {
                        isLoadingData.value = false
                        when (it) {
                            is FirebaseAuthUserCollisionException -> {
                                newEmailError.value = R.string.user_exists
                            }
                            is FirebaseAuthInvalidCredentialsException -> {
                                when {
                                    it.message?.contains("email") == true -> {
                                        newEmailError.value = R.string.not_email_error
                                    }
                                    else -> {
                                        Log.d("MyLog", "Creating $it")
                                        setToastMessage(R.string.unexpected_error)
                                        navigateBack()
                                    }
                                }
                            }
                            else -> {
                                setToastMessage(R.string.unexpected_error)
                                navigateBack()
                            }
                        }
                    }
            }
            ?.addOnFailureListener {
                isLoadingData.value = false
                when (it) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        passwordError.value = R.string.wrong_password_error
                    }
                    is FirebaseNetworkException -> {
                        setToastMessage(R.string.error_no_internet)
                    }
                    else -> {
                        Log.d("MyLog", "Login exception$it")
                        setToastMessage(R.string.unexpected_error)
                        navigateBack()
                    }
                }
            }
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun setNewEmail(newEmail: String) {
        _newEmail.value = newEmail
    }

    fun setConfirmNewEmail(confirmNewEmail: String) {
        _confirmNewEmail.value = confirmNewEmail
    }

    private fun navigateToEmailVerify() {
        navigate(ChangeEmailFragmentDirections.goToEmailVerify())
    }
}