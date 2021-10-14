package pl.kossa.myflights.fragments.profile.settings.nick

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.User
import pl.kossa.myflights.api.requests.UserRequest
import pl.kossa.myflights.api.services.UserService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class ChangeNickViewModel @Inject constructor(
    private val userService: UserService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val _nick = MutableStateFlow("")
    val user = MutableStateFlow<User?>(null)
    val isSaveButtonEnabled = _nick.map {
        return@map it.isNotBlank()
    }

    init {
        fetchUser()
    }

    fun fetchUser() {
        makeRequest(userService::getUser) {
            user.value = it
        }
    }

    fun putUser() {
        makeRequest({
            userService.putUser(
                UserRequest(
                    _nick.value,
                    null // TODO image
                )
            )
        }) {
            toastError.value = R.string.nick_changed
            navigateBack()
        }
    }

    fun setNick(nick: String) {
        _nick.value = nick
    }
}