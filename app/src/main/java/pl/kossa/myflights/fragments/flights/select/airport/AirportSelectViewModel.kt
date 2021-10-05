package pl.kossa.myflights.fragments.flights.select.airport

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.api.models.Airport
import pl.kossa.myflights.api.services.AirportsService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class AirportSelectViewModel @Inject constructor(
    private val airportsService: AirportsService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    val airportsList = MutableStateFlow<List<Airport>>(emptyList())

    init {
        fetchAirports("")
    }

    fun fetchAirports(text: String) {
        makeRequest({airportsService.getAirports(text)}) { it ->
            airportsList.value = it
        }
    }
}