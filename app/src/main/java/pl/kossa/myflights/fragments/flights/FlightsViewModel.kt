package pl.kossa.myflights.fragments.flights

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.fragments.main.MainFragmentDirections
import pl.kossa.myflights.repository.FlightRepository
import pl.kossa.myflights.room.entities.Flight
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class FlightsViewModel @Inject constructor(
    private val flightRepository: FlightRepository,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {


    val flightsList = MutableStateFlow<List<Flight>>(emptyList())

    fun fetchFlights() {
        makeRequest {
            flightsList.value = handleRequest {
                flightRepository.getFlights()
            }
        }
    }

    fun navigateToAddFlight() {
        navigate(MainFragmentDirections.goToFlightAdd())
    }

    fun navigateToFlightDetails(flightId: String) {
        analyticsTracker.logClickFlightDetails()
        navigate(MainFragmentDirections.goToFlightDetails(flightId))
    }

    fun deleteFlight(flightId: String) {
        makeRequest {
            flightRepository.deleteFlight(flightId)
            setToastMessage(R.string.flight_deleted)
            analyticsTracker.logClickDeleteFlight()
            fetchFlights()
        }
    }

    fun navigateToPendingFlights() {
        navigate(MainFragmentDirections.goToPendingSharedFlightsFragment())
    }


}