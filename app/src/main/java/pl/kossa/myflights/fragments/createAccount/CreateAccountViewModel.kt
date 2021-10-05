package pl.kossa.myflights.fragments.createAccount

import android.util.Log
import android.util.Patterns
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val _email = MutableStateFlow("")
    private val _password = MutableStateFlow("")
    private val _confirmPassword = MutableStateFlow("")
    private val _regulationsAccepted = MutableStateFlow(false)
    val isCreateAccountButtonEnabled = combine(
        _email,
        _password,
        _confirmPassword,
        _regulationsAccepted
    ) { email, password, confirmPassword, regulationsAccepted ->
        return@combine email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()
                && (isLoadingData.value?.not() ?: true)
                && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && regulationsAccepted
    }
    val emailError = MutableStateFlow<Int?>(null)
    val passwordError = MutableStateFlow<Int?>(null)

    internal  fun setEmail(email:String) {
        _email.value = email
    }

    internal fun setPassword(password: String) {
        _password.value = password
    }

    internal fun setConfirmPassword(confirmPassword: String) {
        _confirmPassword.value = confirmPassword
    }

    internal fun setRegulationsAccepted(regulationsAccepted: Boolean) {
        _regulationsAccepted.value = regulationsAccepted
    }

    internal fun createAccount() {
        emailError.value = null
        passwordError.value = null
        isLoadingData.value = true
        val email = _email.value
        val password = _password.value
        val confirmPassword = _confirmPassword.value
        when {
            email.isBlank() -> {
                emailError.value = R.string.empty_email
                isLoadingData.value = false
            }
            password.isBlank() -> {
                passwordError.value = R.string.empty_password
                isLoadingData.value = false
            }
            password != confirmPassword -> {
                passwordError.value = R.string.password_not_maches
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
                            passwordError.value = R.string.to_weak_password
                        }
                        is FirebaseAuthUserCollisionException -> {
                            emailError.value = R.string.user_exists
                        }
                        is FirebaseAuthInvalidCredentialsException -> {
                            when {
                                it.message?.contains("email") == true -> {
                                    emailError.value = R.string.not_email_error
                                }
                                else -> {
                                    Log.d("MyLog", "Creating $it")
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