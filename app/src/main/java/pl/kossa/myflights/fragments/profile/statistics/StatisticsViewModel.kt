package pl.kossa.myflights.fragments.profile.statistics

import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {
}