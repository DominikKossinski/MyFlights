package pl.kossa.myflights.fragments.flights.details

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.api.models.Flight
import pl.kossa.myflights.api.services.FlightsService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class FlightDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val flightsService: FlightsService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val fightId = savedStateHandle.get<Int>("flightId")!!

    val flightLiveData = MutableLiveData<Flight>()

    var flight: Flight? = null
        set(value) {
            field = value
            notifyChange()
        }

    fun fetchFlight() {
        makeRequest({
            flightsService.getFLightById(fightId)
        }) { it ->
            flightLiveData.value = it
        }
    }


    @get:Bindable
    val arrivalAirport
        get() = flight?.arrivalRunway?.airport?.name ?: ""
}