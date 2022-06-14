package pl.kossa.myflights.fragments.profile.settings.nick

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.User
import pl.kossa.myflights.api.requests.UserRequest
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.repository.UserRepository
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class ChangeNickViewModel @Inject constructor(
    private val userRepository: UserRepository,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val _nick = MutableStateFlow("")
    val _user = MutableStateFlow<User?>(null)
    val isSaveButtonEnabled = combine(_nick, _user) { nick, user ->
        return@combine nick.isNotBlank() && user != null
    }

    init {
        fetchUser()
    }

    fun fetchUser() {
        makeRequest {
            val response = userRepository.getUser()
            response.body?.let { _user.value = it }
        }
    }

    fun putUser() {
        makeRequest {
            userRepository.putUser(
                UserRequest(
                    _nick.value,
                    _user.value?.avatar?.imageId,
                    _user.value?.regulationsAccepted ?: false
                )
            )
            setToastMessage(R.string.nick_changed)
            navigateBack()
        }
    }

    fun setNick(nick: String) {
        _nick.value = nick
    }
}