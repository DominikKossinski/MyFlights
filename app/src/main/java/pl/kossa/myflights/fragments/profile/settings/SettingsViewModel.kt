package pl.kossa.myflights.fragments.profile.settings

import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    fun navigateToChangeNick() {
        navDirectionLiveData.value = SettingsFragmentDirections.goToChangeNick()
    }

    fun navigateToChangeEmail() {
        navDirectionLiveData.value = SettingsFragmentDirections.goToChangeEmail()
    }

    fun navigateToChangePassword() {
        navDirectionLiveData.value = SettingsFragmentDirections.goToChangePassword()
    }

    fun showAccountDeleteDialog() {
        navDirectionLiveData.value = SettingsFragmentDirections.showAccountDeleteDialog()
    }
}