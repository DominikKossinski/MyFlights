package pl.kossa.myflights.fragments.planned_flights.plan

import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.api.simbrief.SimbriefService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class PlanFlightViewModel @Inject constructor(
    private val simbriefService: SimbriefService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    fun fetchOFP() {
        makeRequest {
            val response = simbriefService.getFlightPlan(1639136372, "1A4303D4A7")
            Log.d("MyLog", "Response: ${response.body}")
        }
    }

    init {
        fetchOFP()
    }
}