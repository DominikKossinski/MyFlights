package pl.kossa.myflights.dialogs.avatar

import android.net.Uri
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.api.server.models.User
import pl.kossa.myflights.api.server.requests.UserRequest
import pl.kossa.myflights.api.server.services.UserService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class ChangeAvatarViewModel @Inject constructor(
    private val userService: UserService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    val user = MutableStateFlow<User?>(null)

    init {
        fetchUser()
    }

    fun fetchUser() {
        makeRequest {
            val response = userService.getUser()
            user.value = response.body
        }
    }

    fun deleteAvatar() {
        makeRequest {
            val nick = user.value?.nick ?: ""
            userService.putUser(UserRequest(nick, null))
            navigateBack()
        }
    }

    fun navigateToAcceptAvatar(uri: Uri) {
        navigate           (ChangeAvatarBottomSheetDirections.goToAcceptAvatar(uri.toString()))
    }
}