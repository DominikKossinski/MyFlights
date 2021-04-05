package pl.kossa.myflights.fragments.flights

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.api.models.Flight
import pl.kossa.myflights.api.services.FlightsService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class FlightsViewModel @Inject constructor(
    private val flightsService: FlightsService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {


    val flightsList = MutableLiveData<List<Flight>>()

    fun fetchFlights() {
        makeRequest(flightsService::getAllFlights) {
            flightsList.value = it
        }
    }


}