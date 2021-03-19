package pl.kossa.myflights.fragments.flights

import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.api.ApiService
import pl.kossa.myflights.api.models.Flight
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class FlightsViewModel @Inject constructor(
    private val apiService: ApiService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {


    val flightsList = MutableLiveData<List<Flight>>()

    fun fetchFlights() {
        makeRequest(apiService.service::getAllFlights) {
            flightsList.value = it
        }
    }


}