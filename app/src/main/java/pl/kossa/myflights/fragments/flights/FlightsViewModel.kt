package pl.kossa.myflights.fragments.flights

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import pl.kossa.myflights.api.models.Flight
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper

class FlightsViewModel(
    navController: NavController,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(navController, preferencesHelper) {


    val flightsList = MutableLiveData<List<Flight>>()

    fun fetchFlights() {
        makeRequest(apiService.service::getAllFlights) {
            Log.d("MyLog", "Flights: $it")
            flightsList.value = it
        }
    }


}