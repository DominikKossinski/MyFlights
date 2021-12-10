package pl.kossa.myflights.fragments.planned_flights.ofp

import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class OFPDetailsViewModel @Inject constructor(preferencesHelper: PreferencesHelper) : BaseViewModel(preferencesHelper) {
}