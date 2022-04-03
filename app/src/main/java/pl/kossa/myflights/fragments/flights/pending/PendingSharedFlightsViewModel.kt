package pl.kossa.myflights.fragments.flights.pending

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.api.responses.sharedflights.SharedFlightResponse
import pl.kossa.myflights.api.services.SharedFlightsService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class PendingSharedFlightsViewModel @Inject constructor(
    private val sharedFlightsService: SharedFlightsService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    val pendingSharedFlights = MutableStateFlow<List<SharedFlightResponse>>(emptyList())

    init {
        fetchPendingSharedFlights()
    }

    fun fetchPendingSharedFlights() {
        makeRequest {
            val response = sharedFlightsService.getPendingSharedFlights()
            response.body?.let { pendingSharedFlights.value = it }
        }
    }
}