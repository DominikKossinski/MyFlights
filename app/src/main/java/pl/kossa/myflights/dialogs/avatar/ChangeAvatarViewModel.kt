package pl.kossa.myflights.dialogs.avatar

import android.net.Uri
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.api.models.User
import pl.kossa.myflights.api.requests.UserRequest
import pl.kossa.myflights.api.services.UserService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.repository.UserRepository
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class ChangeAvatarViewModel @Inject constructor(
    private val userRepository: UserRepository,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    val user = MutableStateFlow<User?>(null)

    init {
        fetchUser()
    }

    fun fetchUser() {
        makeRequest {
            val response = handleRequest {
                userRepository.getUser()
            }
            user.value = response
        }
    }

    fun deleteAvatar() {
        makeRequest {
            val nick = user.value?.nick ?: ""
            val regulationsAccepted = user.value?.regulationsAccepted ?: false
            val response = handleRequest {
                userRepository.putUser(UserRequest(nick, null, regulationsAccepted))
            }
           response?.let { navigateBack() }
        }
    }

    fun navigateToAcceptAvatar(uri: Uri) {
        navigate(ChangeAvatarBottomSheetDirections.goToAcceptAvatar(uri.toString()))
    }
}