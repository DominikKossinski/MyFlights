package pl.kossa.myflights.fragments.planned_flights.plan

import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.api.simbrief.services.SimbriefService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.fragments.main.MainFragmentDirections
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class PlanFlightViewModel @Inject constructor(
    private val simbriefService: SimbriefService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    fun navigateToOFPDetails() {
        navigate(MainFragmentDirections.goToOFPDetailsFragment(1639227145, "1A4303D4A7"))
    }

}