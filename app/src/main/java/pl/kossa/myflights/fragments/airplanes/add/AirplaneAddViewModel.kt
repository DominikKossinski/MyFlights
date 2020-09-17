package pl.kossa.myflights.fragments.airplanes.add

import androidx.databinding.Bindable
import androidx.navigation.NavController
import pl.kossa.myflights.BR
import pl.kossa.myflights.R
import pl.kossa.myflights.api.requests.AirplaneRequest
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.fragments.airplanes.AirplanesFragmentDirections
import pl.kossa.myflights.utils.PreferencesHelper

class AirplaneAddViewModel(
    navController: NavController,
    preferencesHelper: PreferencesHelper
) :
    BaseViewModel(navController, preferencesHelper) {


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

    @get:Bindable
    var maxSpeed: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.maxSpeed)
            }
        }

    @get:Bindable
    var weight: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.weight)
            }
        }


    fun postAirplane() {
        nameError = null
        if (name.isBlank()) {
            nameError = R.string.error_empty_name
            return
        }
        //TODO image
        val request = AirplaneRequest(name, maxSpeed?.toInt(), weight?.toInt(), null)
        makeRequest({
            apiService.airplanesService.postAirplane(request)
        }) {
            navigateToDetails(it.entityId)
        }
    }

    private fun navigateToDetails(airplaneId: Int) {
        navController.popBackStack()
        navController.navigate(AirplanesFragmentDirections.goToAirplaneDetails(airplaneId))
    }
}
