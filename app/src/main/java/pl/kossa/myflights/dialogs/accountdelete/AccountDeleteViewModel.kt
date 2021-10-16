package pl.kossa.myflights.dialogs.accountdelete

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import okhttp3.ResponseBody
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.User
import pl.kossa.myflights.api.responses.ApiErrorBody
import pl.kossa.myflights.api.services.UserService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import retrofit2.Converter
import javax.inject.Inject

@HiltViewModel
class AccountDeleteViewModel @Inject constructor(
    private val userService: UserService,
    errorBodyConverter: Converter<ResponseBody, ApiErrorBody>,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(errorBodyConverter, preferencesHelper) {

    private val user = MutableStateFlow<User?>(null)
    private val _password = MutableStateFlow("")
    val passwordError = MutableStateFlow<Int?>(null)
    val isDeleteButtonEnabled = combine(_password, user) {
        password, user ->
        return@combine password.isNotBlank() && user != null
    }

    init {
        fetchUser()
    }

    private fun fetchUser() {
        makeRequest(userService::getUser) {
            user.value = it
        }
    }

    fun deleteAccount() {
        val email = user.value?.email ?: return
        isLoadingData.value = true
        val credential = EmailAuthProvider.getCredential(email, _password.value)
        firebaseAuth.currentUser?.reauthenticate(credential)
            ?.addOnSuccessListener {
                makeRequest(userService::deleteUser) {
                    isLoadingData.value = false
                    firebaseAuth.currentUser?.delete()
                    signOut()
                }
            }
            ?.addOnFailureListener {
                isLoadingData.value = false
                when (it) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        passwordError.value = R.string.wrong_password_error
                    }
                    is FirebaseNetworkException -> {
                        setToastError(R.string.error_no_internet)
                    }
                    else -> {
                        Log.d("MyLog", "Login exception$it")
                        setToastError(R.string.unexpected_error)
                        navigateBack()
                    }
                }
            }
    }

    fun setPassword(password: String) {
        _password.value = password
    }
}