package pl.kossa.myflights.fragments.flights.users

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.api.responses.flights.FlightResponse
import pl.kossa.myflights.api.services.FlightsService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

class SharedUsersViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val flightsService: FlightsService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val flightId = savedStateHandle.get<String>("flightId")!!

    val flightResponse = MutableStateFlow<FlightResponse?>(null)

    init {
        fetchFlight()
    }

    fun fetchFlight() {
        makeRequest {
            val response = flightsService.getFLightById(flightId)
            response.body?.let { flightResponse.value = it }
        }
    }

    fun getUserId(): String? = currentUser?.uid

}