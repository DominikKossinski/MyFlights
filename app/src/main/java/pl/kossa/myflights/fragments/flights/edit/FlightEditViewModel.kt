package pl.kossa.myflights.fragments.flights.edit

import androidx.lifecycle.SavedStateHandle
import pl.kossa.myflights.api.services.FlightsService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

class FlightEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val flightsService: FlightsService,
    preferencesHelper: PreferencesHelper
): BaseViewModel(preferencesHelper) {

    private val flightId = savedStateHandle.get<String>("flightId")!!

    init {
        fetchFlight()
    }

    fun fetchFlight() {
        //tODO

    }
}