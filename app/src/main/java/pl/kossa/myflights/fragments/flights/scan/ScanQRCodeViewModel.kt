package pl.kossa.myflights.fragments.flights.scan

import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class ScanQRCodeViewModel @Inject constructor(
    preferencesHelper: PreferencesHelper
): BaseViewModel(preferencesHelper) {

}