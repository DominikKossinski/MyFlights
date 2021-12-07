package pl.kossa.myflights.fragments.profile.settings.about_app

import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class AboutAppViewModel @Inject constructor(
    preferencesHelper: PreferencesHelper
): BaseViewModel(preferencesHelper) {
}