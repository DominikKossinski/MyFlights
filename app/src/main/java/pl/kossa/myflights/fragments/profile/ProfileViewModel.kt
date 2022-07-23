package pl.kossa.myflights.fragments.profile

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.fragments.main.MainFragmentDirections
import pl.kossa.myflights.repository.UserRepository
import pl.kossa.myflights.utils.PreferencesHelper
import pl.kossa.myflights.utils.config.MyFlightsRemoteConfig
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    remoteConfig: MyFlightsRemoteConfig,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    val email = MutableStateFlow(preferencesHelper.email ?: "")
    val nick = MutableStateFlow(preferencesHelper.nick ?: "")
    val avatar = MutableStateFlow<String?>(preferencesHelper.avatar?.url)
    val buttonText = remoteConfig.getButtonText()


    init {
        fetchUser()
    }

    fun fetchUser() {
        makeRequest {
            val response = handleRequest {
                userRepository.getUser()
            }
            response?.let {
                setUser(it)
                email.emit(it.email ?: "")
                nick.emit(it.nick)
                avatar.emit(it.avatar?.url)
            }
        }
    }

    fun navigateToSettings() {
        analyticsTracker.logClickSettings()
        navigate(MainFragmentDirections.goToSettings())
    }

    fun showChangeAccountBottomSheet() {
        navigate(MainFragmentDirections.showChangeAvatarBottomSheet())
    }

    fun navigateToStatistics() {
        analyticsTracker.logClickStatistics()
        navigate(MainFragmentDirections.goToStatistics())
    }

    fun logSignOut() {
        analyticsTracker.logClickSignOut()
    }
}