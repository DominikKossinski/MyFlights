package pl.kossa.myflights.dialogs.request

import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class JoinRequestSentViewModel @Inject constructor(
    preferencesHelper: PreferencesHelper
): BaseViewModel(preferencesHelper) {

}