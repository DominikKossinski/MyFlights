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
        navigate(SettingsFragmentDirections.goToAboutApp())
    }
}