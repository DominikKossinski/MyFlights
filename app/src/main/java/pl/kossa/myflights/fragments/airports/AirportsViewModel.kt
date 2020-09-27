package pl.kossa.myflights.fragments.airports

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import pl.kossa.myflights.BR
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Airport
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper

class AirportsViewModel(navController: NavController, preferencesHelper: PreferencesHelper) :
    BaseViewModel(navController, preferencesHelper) {

    val airportsList = MutableLiveData<List<Airport>>()

    fun fetchAirports() {
        makeRequest(apiService.airportsService::getAllAirports) {
            airportsList.value = it
        }
    }

    @get:Bindable
    var noAirportsVisibility = false
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.noAirportsVisibility)
            }
        }


    fun deleteAirport(airportId: Int) {
        makeRequest({ apiService.airportsService.deleteAirort(airportId) }) {
            toastError.value = R.string.airport_deleted
            fetchAirports()
        }
    }

    fun navigateToAddAirport() {
        navController.navigate(AirportsFragmentDirections.goToAirportAdd())
    }

    fun navigateToAirportDetails(airportId: Int) {
        navController.navigate(AirportsFragmentDirections.goToAirportDetails(airportId))
    }
}