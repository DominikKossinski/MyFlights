package pl.kossa.myflights.fragments.profile

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.api.models.User
import pl.kossa.myflights.api.services.UserService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.fragments.main.MainFragmentDirections
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
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
            response.body?.let {
                user.emit(it)
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