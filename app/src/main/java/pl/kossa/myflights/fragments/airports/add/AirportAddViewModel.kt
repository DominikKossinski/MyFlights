package pl.kossa.myflights.fragments.airports.add

import android.util.Log
import androidx.databinding.Bindable
import androidx.navigation.NavController
import pl.kossa.myflights.BR
import pl.kossa.myflights.api.requests.AirportRequest
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.fragments.airports.AirportsFragmentDirections
import pl.kossa.myflights.utils.PreferencesHelper

class AirportAddViewModel(
    navController: NavController,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(navController, preferencesHelper) {


    @get:Bindable
    var name = ""
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.name)
            }
        }

    @get:Bindable
    var nameError: Int? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.nameError)
            }
        }


    fun postAirport() {
        //TODO add data
        makeRequest({
            val request = AirportRequest(name, "", "", "", "", null)
            apiService.airportsService.postAirport(request)
        }) { it ->
            navigateToDetails(it.entityId)
        }
    }

    private fun navigateToDetails(airportId: Int) {
        Log.d("MyLog", "AirportId: $airportId")
        navController.popBackStack()
        navController.navigate(AirportsFragmentDirections.goToAirportDetails(airportId))
    }
}
