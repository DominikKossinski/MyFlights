package pl.kossa.myflights.fragments.flights.select.airport

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.repository.AirportRepository
import pl.kossa.myflights.room.entities.Airport
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class AirportSelectViewModel @Inject constructor(
    private val airportsRepository: AirportRepository,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    val airportsList = MutableStateFlow<List<Airport>>(emptyList())

    init {
        fetchAirports("")
    }

    fun fetchAirports(text: String) {
        makeRequest {
            //TODO filtering by text
            airportsList.value = handleRequest {
                airportsRepository.getAirports()
            }
        }
    }
}