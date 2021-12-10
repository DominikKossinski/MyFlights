package pl.kossa.myflights.fragments.airports

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.R
import pl.kossa.myflights.api.server.models.Airport
import pl.kossa.myflights.api.server.services.AirportsService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.fragments.main.MainFragmentDirections
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class AirportsViewModel @Inject constructor(
    private val airportsService: AirportsService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    val airportsList = MutableStateFlow<List<Airport>>(emptyList())

    fun fetchAirports() {
        makeRequest {
            val response = airportsService.getAirports()
            response.body?.let { airportsList.value = it }
        }
    }

    fun deleteAirport(airportId: String) {
        makeRequest {
            airportsService.deleteAirport(airportId)
            setToastMessage(R.string.airport_deleted)
            fetchAirports()
        }
    }

    fun navigateToAddAirport() {
        navigate(MainFragmentDirections.goToAirportAdd())
    }

    fun navigateToAirportDetails(airportId: String) {
        navigate(MainFragmentDirections.goToAirportDetails(airportId))
    }
}