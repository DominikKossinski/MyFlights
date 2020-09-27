package pl.kossa.myflights.fragments.airports.details

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Airport
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper

class AirportDetailsViewModel(
    private val airportId: Int,
    navController: NavController,
    preferencesHelper: PreferencesHelper
) :
    BaseViewModel(navController, preferencesHelper) {

    init {
        fetchAirport()
    }

    val airportLiveData = MutableLiveData<Airport>()

    var airport: Airport? = null
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    val airportName
        get() = airport?.name ?: ""

    fun fetchAirport() {
        makeRequest({
            apiService.airportsService.getAirportById(airportId)
        }) { it ->
            airportLiveData.value = it
        }
    }

    fun navigateToAirportEdit() {
        navController.navigate(AirportDetailsFragmentDirections.goToAirportEdit(airportId))
    }

    fun deleteAirport() {
        makeRequest({
            apiService.airportsService.deleteAirort(airportId)
        }
        ) {
            toastError.value = R.string.airport_deleted
            navigateBack()
        }
    }
}
