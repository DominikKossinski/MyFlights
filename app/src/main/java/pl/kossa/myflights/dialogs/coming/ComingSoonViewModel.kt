package pl.kossa.myflights.dialogs.coming

import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class ComingSoonViewModel @Inject constructor(
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {
}