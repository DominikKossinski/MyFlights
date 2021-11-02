package pl.kossa.myflights.fragments.prelogin

import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class PreLoginViewModel @Inject constructor(
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    fun navigateToLogin() {
        navigate           (PreLoginFragmentDirections.goToLoginFragment())
    }

    fun navigateToCrateAccount() {
        navigate           (PreLoginFragmentDirections.goToCreateAccount())
    }
}