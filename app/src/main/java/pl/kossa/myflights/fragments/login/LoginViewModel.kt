package pl.kossa.myflights.fragments.login

import android.util.Log
import androidx.databinding.Bindable
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.BR
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    @Bindable
    var loginButtonEnabled = false
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.loginButtonEnabled)
            }
        }

    @Bindable
    var emailError: Int? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.emailError)
            }
        }

    @Bindable
    var passwordError: Int? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.passwordError)
            }
        }


    @Bindable
    var email: String = ""
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.email)
                setLoginButtonEnabled()
            }
        }

    @Bindable
    var password: String = ""
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.password)
                setLoginButtonEnabled()
            }
        }

    private fun setLoginButtonEnabled() {
        loginButtonEnabled =
            email.isNotBlank() && password.isNotBlank() && isLoadingData.value?.not() ?: true
    }

    fun navigateToCreateAccount() {
        navDirectionLiveData.value = LoginFragmentDirections.goToCreateAccount()
    }

    internal fun login() {
        emailError = null
        passwordError = null
        isLoadingData.value = true
        when {
            email.isBlank() -> {
                emailError = R.string.empty_email
                isLoadingData.value = false
            }
            password.isBlank() -> {
                passwordError = R.string.empty_password
                isLoadingData.value = false
            }
            else -> {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                    if (firebaseAuth.currentUser != null && firebaseAuth.currentUser?.isEmailVerified ?: false) {
                        refreshToken {
                            Log.d("MyLog", "Token Login: $it")
                            navDirectionLiveData.value = LoginFragmentDirections.goToMainActivity()
                            isLoadingData.value = false
                        }
                    } else {
                        resendVerificationEmail()
                    }
                }.addOnFailureListener {
                    when (it) {
                        is FirebaseAuthInvalidUserException -> {
                            emailError = R.string.no_user_error
                            password = ""
                        }
                        is FirebaseAuthInvalidCredentialsException -> {
                            when {
                                it.message?.contains("email") == true -> {
                                    emailError = R.string.not_email_error
                                }
                                it.message?.contains("password") == true -> {
                                    passwordError = R.string.wrong_password_error
                                }
                                else -> {
                                    Log.d("MyLog", "Login exception$it")
                                    toastError.value = R.string.unexpected_error
                                }
                            }
                        }
                        is FirebaseNetworkException -> {
                            toastError.value = R.string.no_internet_error
                        }
                        else -> {

                            Log.d("MyLog", "Login exception$it")
                            toastError.value = R.string.unexpected_error
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
                is FirebaseNetworkException -> toastError.value =
                    R.string.no_internet_error
                else -> {
                    toastError.value = R.string.unexpected_error
                }
            }
        }
    }

    private fun navigateToResendEmail() {
        navDirectionLiveData.value = LoginFragmentDirections.goToEmailResend()
    }
}