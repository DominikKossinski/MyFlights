package pl.kossa.myflights.fragments.flights.join

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.api.responses.sharedflights.SharedFlightJoinDetails
import pl.kossa.myflights.api.services.SharedFlightsService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class JoinFlightViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sharedFlightsService: SharedFlightsService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val sharedFlightId = savedStateHandle.get<String>("sharedFlightId")!!

    val sharedFlightJoinDetails = MutableStateFlow<SharedFlightJoinDetails?>(null)

    init {
        fetchSharedFlightJoinDetails()
    }

    private fun fetchSharedFlightJoinDetails() {
        makeRequest {
            val response = sharedFlightsService.getSharedFlightJoinDetails(sharedFlightId)
            response.body?.let { sharedFlightJoinDetails.value = it }
        }
    }

    fun joinSharedFlight() {
        makeRequest {
            sharedFlightsService.joinSharedFlight(sharedFlightId)
            // TODO show confirmation waiting dialog
        }
    }
}