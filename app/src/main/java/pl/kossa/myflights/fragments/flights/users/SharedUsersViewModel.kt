package pl.kossa.myflights.fragments.flights.users

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.repository.FlightRepository
import pl.kossa.myflights.room.entities.Flight
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class SharedUsersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val flightRepository: FlightRepository,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val flightId = savedStateHandle.get<String>("flightId")!!

    val flight = MutableStateFlow<Flight?>(null)

    init {
        fetchFlight()
    }

    fun fetchFlight() {
        makeRequest {
            flight.value = handleRequest {
                flightRepository.getFlightById(flightId)
            }
        }
    }

    fun getUserId(): String? = currentUser?.uid

}