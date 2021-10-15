package pl.kossa.myflights.fragments.profile.settings.password

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.User
import pl.kossa.myflights.api.services.UserService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(
    private val userService: UserService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val _password = MutableStateFlow("")
    val passwordError = MutableStateFlow<Int?>(null)
    private val _newPassword = MutableStateFlow("")
    val newPasswordError = MutableStateFlow<Int?>(null)
    private val _confirmNewPassword = MutableStateFlow("")
    private val _user = MutableStateFlow<User?>(null)

    val isSaveButtonEnabled = combine(
        _password,
        _newPassword,
        _confirmNewPassword,
        _user
    ) { password, newPassword, confirmNewPassword, user ->
        return@combine password.isNotBlank() && newPassword.isNotBlank() && newPassword.contentEquals(
            confirmNewPassword
        ) && user != null
    }

    init {
        fetchUser()
    }

    fun changePassword() {
        isLoadingData.value = true
        passwordError.value = null
        newPasswordError.value = null
        val email = _user.value?.email
        if (email == null) {
            isLoadingData.value = false
            return
        }
        val credential = EmailAuthProvider.getCredential(email, _password.value)
        firebaseAuth.currentUser?.reauthenticate(credential)
            ?.addOnSuccessListener {
                firebaseAuth.currentUser?.updatePassword(_newPassword.value)
                    ?.addOnSuccessListener {
                        isLoadingData.value = false
                        toastError.value = R.string.password_changed
                        navigateBack()
                    }
                    ?.addOnFailureListener {
                        isLoadingData.value = false
                        when (it) {
                            is FirebaseAuthWeakPasswordException -> {
                                newPasswordError.value = R.string.to_weak_password
                            }
                            else -> {
                                toastError.value = R.string.unexpected_error
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
                        toastError.value = R.string.no_internet_error
                    }
                    else -> {
                        Log.d("MyLog", "Login exception$it")
                        toastError.value = R.string.unexpected_error
                        navigateBack()
                    }


                }
            }
    }

    fun fetchUser() {
        makeRequest(userService::getUser) {
            _user.value = it
        }
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun setNewPassword(newPassword: String) {
        _newPassword.value = newPassword
    }

    fun setConfirmNewPassword(confirmNewPassword: String) {
        _confirmNewPassword.value = confirmNewPassword
    }
}