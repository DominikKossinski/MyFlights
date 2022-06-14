package pl.kossa.myflights.fragments.fill_profile

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import pl.kossa.myflights.api.models.User
import pl.kossa.myflights.api.requests.UserRequest
import pl.kossa.myflights.api.services.UserService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.repository.UserRepository
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class FillProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    val user = MutableStateFlow<User?>(null)
    private val _nick = MutableStateFlow("")
    private val _regulationsAccepted = MutableStateFlow(false)
    val isCreateAccountEnabled = combine(
        _nick,
        _regulationsAccepted,
        isLoadingData,
    ) { nick, regulationsAccepted, isLoading ->
        return@combine nick.isNotBlank() && regulationsAccepted && !isLoading
    }

    fun fetchUser() {
        makeRequest {
            val response = userRepository.getUser()
            response.body?.let {
                user.emit(it)
            }
        }
    }

    fun createAccount() {
        makeRequest {
            val imageId = user.value?.avatar?.imageId
            userRepository.putUser(
                UserRequest(_nick.value, imageId, _regulationsAccepted.value)
            )
            navigate(FillProfileFragmentDirections.goToMainActivity(), true)
        }
    }

    fun setNick(nick: String) {
        _nick.value = nick
    }

    fun setRegulationsAccepted(regulationsAccepted: Boolean) {
        _regulationsAccepted.value = regulationsAccepted
    }
}