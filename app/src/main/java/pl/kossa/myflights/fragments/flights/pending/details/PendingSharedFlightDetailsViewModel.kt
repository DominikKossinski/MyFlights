package pl.kossa.myflights.fragments.flights.pending.details

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.sharedflights.SharedFlightResponse
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.repository.SharedFlightRepository
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class PendingSharedFlightDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sharedFlightRepository: SharedFlightRepository,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val sharedFlightId = savedStateHandle.get<String>("sharedFlightId")!!
    val sharedFlight = MutableStateFlow<SharedFlightResponse?>(null)

    init {
        fetchSharedFlight()
    }

    fun fetchSharedFlight() {
        makeRequest {
            val response = handleRequest {
                sharedFlightRepository.getSharedFlight(sharedFlightId)
            }
            sharedFlight.value = response
        }
    }

    fun deleteSharedFlight() {
        makeRequest {
            sharedFlightRepository.deleteSharedFlight(sharedFlightId)
            navigateBack()
        }
    }

    fun confirmSharedFlight() {
        makeRequest {
            val response = handleRequest {
                sharedFlightRepository.confirmSharedFlight(sharedFlightId)
            }
            response?.let {
                setToastMessage(R.string.pending_shared_flight_confirmed)
                navigateBack()
            }
        }
    }
}