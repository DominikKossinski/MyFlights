package pl.kossa.myflights.dialogs.accountdelete

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.ProviderType
import pl.kossa.myflights.api.models.User
import pl.kossa.myflights.api.services.UserService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.repository.UserRepository
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class AccountDeleteViewModel @Inject constructor(
    private val userRepository: UserRepository,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val user = MutableStateFlow<User?>(null)
    private val _password = MutableStateFlow("")
    val passwordError = MutableStateFlow<Int?>(null)
    val providerType = user.map { it?.providerType }
    val isDeleteButtonEnabled = combine(_password, user) { password, user ->
        return@combine user != null && (password.isNotBlank() || user.providerType == ProviderType.GOOGLE)
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

    fun deleteAccount() {
        val email = user.value?.email ?: return
        isLoadingData.value = true
        val credential = EmailAuthProvider.getCredential(email, _password.value)
        deleteUserWithCredential(credential)
    }

    fun deleteUserWithCredential(credential: AuthCredential) {
        firebaseAuth.currentUser?.reauthenticate(credential)
            ?.addOnSuccessListener {
                makeRequest {
                    val response = handleRequest {
                        userRepository.deleteUser()
                    }
                    response?.let {
                        analyticsTracker.logClickDeleteAccount()
                        isLoadingData.value = false
                        signOut()
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
                        setToastMessage(R.string.unexpected_error)
                        navigateBack()
                    }
                }
            }
    }

    fun getAuthProviderType(): ProviderType? {
        return user.value?.providerType
    }

    fun setPassword(password: String) {
        _password.value = password
    }
}