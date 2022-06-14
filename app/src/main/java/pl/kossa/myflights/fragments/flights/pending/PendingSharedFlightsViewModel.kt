package pl.kossa.myflights.fragments.flights.pending

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.sharedflights.SharedFlightResponse
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.repository.SharedFlightRepository
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class PendingSharedFlightsViewModel @Inject constructor(
    private val sharedFlightRepository: SharedFlightRepository,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    val pendingSharedFlights = MutableStateFlow<List<SharedFlightResponse>>(emptyList())

    init {
        fetchPendingSharedFlights()
    }

    fun fetchPendingSharedFlights() {
        makeRequest {
            val response = sharedFlightRepository.getPendingSharedFlights()
            response.body?.let { pendingSharedFlights.value = it }
        }
    }

    fun navigateToPendingSharedFlightDetails(sharedFlightId: String) {
        navigate(
            PendingSharedFlightsFragmentDirections.goToPendingSharedFlightsDetails(
                sharedFlightId
            )
        )
    }

    fun deleteSharedFlight(sharedFlightId: String) {
        makeRequest {
            sharedFlightRepository.deleteSharedFlight(sharedFlightId)
            fetchPendingSharedFlights()
        }
    }

    fun confirmSharedFlight(sharedFlightId: String) {
        makeRequest {
            sharedFlightRepository.confirmSharedFlight(sharedFlightId)
            setToastMessage(R.string.pending_shared_flight_confirmed)
            fetchPendingSharedFlights()
        }
    }

    fun getUserId(): String? = currentUser?.uid
}