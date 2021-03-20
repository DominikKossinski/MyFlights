package pl.kossa.myflights.fragments.createAccount

import android.util.Log
import androidx.databinding.Bindable
import androidx.navigation.NavController
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.BR
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    @Bindable
    var createAccountButtonEnabled = false
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.createAccountButtonEnabled)
            }
        }

    @Bindable
    var email = ""
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.email)
                setCreateAccountButtonEnabled()
            }
        }

    @Bindable
    var password = ""
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.password)
                setCreateAccountButtonEnabled()
            }
        }

    @Bindable
    var confirmedPassword = ""
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.confirmedPassword)
                setCreateAccountButtonEnabled()
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


    private fun setCreateAccountButtonEnabled() {
        createAccountButtonEnabled =
            email.isNotBlank() && password.isNotBlank() && confirmedPassword.isNotBlank() && isLoadingData.value?.not() ?: true
    }

    internal fun createAccount() {
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
            password != confirmedPassword -> {
                passwordError = R.string.password_not_maches
                isLoadingData.value = false
            }
            else -> {
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                    firebaseAuth.currentUser?.sendEmailVerification()?.addOnSuccessListener {
                        navigateToEmailResend()
                    }?.addOnFailureListener {
                        when (it) {
                            is FirebaseNetworkException -> toastError.value =
                                R.string.no_internet_error
                            else -> {
                                toastError.value = R.string.unexpected_error
                            }
                        }
                    }
                }.addOnFailureListener {
                    isLoadingData.value = false
                    when (it) {
                        is FirebaseAuthWeakPasswordException -> {
                            passwordError = R.string.to_weak_password
                        }
                        is FirebaseAuthUserCollisionException -> {
                            emailError = R.string.user_exists
                        }
                        is FirebaseAuthInvalidCredentialsException -> {
                            when {
                                it.message?.contains("email") == true -> {
                                    emailError = R.string.not_email_error
                                }
                                else -> {
                                    Log.d("MyLog", "Creatind $it")
                                    toastError.value = R.string.unexpected_error
                                }
                            }
                        }
                        is FirebaseNetworkException -> toastError.value = R.string.no_internet_error
                        else -> {
                            Log.d("MyLog", "Creating account exception: $it")
                        }
                    }
                }
            }
        }
    }

    private fun navigateToEmailResend() {
        navDirectionLiveData.value = CreateAccountFragmentDirections.goToEmailResend()
    }
}