package pl.kossa.myflights.fragments.flights.select.runway

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.repository.AirportRepository
import pl.kossa.myflights.room.entities.Airport
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class RunwaySelectViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val airportRepository: AirportRepository,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val airportId = savedStateHandle.get<String>("airportId")!!
    val airport = MutableStateFlow<Airport?>(null)

    init {
        fetchAirport()
    }

    fun fetchAirport() {
        makeRequest {
            airport.value = airportRepository.getAirportById(airportId)
        }
    }

}