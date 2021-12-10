package pl.kossa.myflights.fragments.flights.details

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.R
import pl.kossa.myflights.api.server.models.Flight
import pl.kossa.myflights.api.server.services.FlightsService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class FlightDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val flightsService: FlightsService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val flightId = savedStateHandle.get<String>("flightId")!!

    val flight = MutableStateFlow<Flight?>(null)

    fun fetchFlight() {
        makeRequest {
            val response = flightsService.getFLightById(flightId)
            response.body?.let { flight.value = it }
        }
    }

    fun navigateToFlightEdit() {
        navigate(FlightDetailsFragmentDirections.goToFlightEdit(flightId))
    }

    fun deleteFlight() {
        makeRequest {
            flightsService.deleteFlight(flightId)
            setToastMessage(R.string.flight_deleted)
            navigateBack()
        }
    }

}