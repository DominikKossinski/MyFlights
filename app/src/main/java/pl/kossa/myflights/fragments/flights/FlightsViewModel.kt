package pl.kossa.myflights.fragments.flights

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Flight
import pl.kossa.myflights.api.services.FlightsService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.fragments.main.MainFragmentDirections
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class FlightsViewModel @Inject constructor(
    private val flightsService: FlightsService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {


    val flightsList = MutableStateFlow<List<Flight>>(emptyList())

    init {
        fetchFlights()
    }

    fun fetchFlights() {
        makeRequest {
            val response = flightsService.getAllFlights()
            response.body?.let { flightsList.value = it }
        }
    }

    fun navigateToAddFlight() {
        navigate(MainFragmentDirections.goToFlightAdd())
    }

    fun navigateToFlightDetails(flightId: String) {
        navigate(MainFragmentDirections.goToFlightDetails(flightId))
    }

    fun deleteFlight(flightId: String) {
        makeRequest {
            flightsService.deleteFlight(flightId)
            setToastMessage(R.string.flight_deleted)
            fetchFlights()
        }
    }


}