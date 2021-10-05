package pl.kossa.myflights.fragments.airports

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Airport
import pl.kossa.myflights.api.services.AirportsService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.fragments.main.MainFragmentDirections
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class AirportsViewModel @Inject constructor(
    private val airportsService: AirportsService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    val airportsList = MutableLiveData<List<Airport>>()

    fun fetchAirports() {
        makeRequest(airportsService::getAirports) {
            airportsList.value = it
        }
    }

    fun deleteAirport(airportId: String) {
        makeRequest({ airportsService.deleteAirport(airportId) }) {
            toastError.value = R.string.airport_deleted
            fetchAirports()
        }
    }

    fun navigateToAddAirport() {
        navDirectionLiveData.value = MainFragmentDirections.goToAirportAdd()
    }

    fun navigateToAirportDetails(airportId: String) {
        navDirectionLiveData.value = MainFragmentDirections.goToAirportDetails(airportId)
    }
}