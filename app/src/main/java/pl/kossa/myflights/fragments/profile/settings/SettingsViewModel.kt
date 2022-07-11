package pl.kossa.myflights.fragments.profile.settings

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import pl.kossa.myflights.api.models.ProviderType
import pl.kossa.myflights.api.models.User
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.repository.UserRepository
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val _user = MutableStateFlow<User?>(null)
    val isChangeEnabled = _user.map {
        it != null && it.providerType == ProviderType.PASSWORD
    }

    init {
        fetchUser()
    }

    private fun fetchUser() {
        makeRequest {
            if (currentUser != null) {
                val response = handleRequest {
                    userRepository.getUser()
                }
                _user.value = response
            }
        }
    }

    fun navigateToChangeNick() {
        navigate(SettingsFragmentDirections.goToChangeNick())
    }

    fun navigateToChangeEmail() {
        navigate(SettingsFragmentDirections.goToChangeEmail())
    }

    fun navigateToChangePassword() {
        navigate(SettingsFragmentDirections.goToChangePassword())
    }

    fun showAccountDeleteDialog() {
        navigate(SettingsFragmentDirections.showAccountDeleteDialog())
    }

    fun navigateToAboutApp() {
        analyticsTracker.logClickAboutApp()
        navigate(SettingsFragmentDirections.goToAboutApp())
    }
}