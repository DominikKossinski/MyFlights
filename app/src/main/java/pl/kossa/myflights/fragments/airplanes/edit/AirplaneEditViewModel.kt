package pl.kossa.myflights.fragments.airplanes.edit

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import pl.kossa.myflights.BR
import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.api.requests.AirplaneRequest
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper

class AirplaneEditViewModel(
    private val airplaneId: Int,
    navController: NavController,
    preferencesHelper: PreferencesHelper
) :
    BaseViewModel(navController, preferencesHelper) {

    init {
        fetchAirplane()
    }

    val airplaneLiveData = MutableLiveData<Airplane>()

    var airplane: Airplane? = null
        set(value) {
            field = value
            if (value != null) {
                name = value.name
                maxSpeed = if (value.maxSpeed != null) value.maxSpeed.toString() else null
                weight = if (value.weight != null) value.weight.toString() else null
            }
            notifyChange()
        }

    @get:Bindable
    var name = ""
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.airplaneName)
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

    private fun fetchAirplane() {
        makeRequest({
            apiService.airplanesService.getAirplaneById(airplaneId)
        }) { it ->
            airplaneLiveData.value = it
        }
    }

    fun saveAirplane() {
        makeRequest({
            //TODO image
            val request = AirplaneRequest(name, maxSpeed?.toInt(), weight?.toInt(), null)
            apiService.airplanesService.putAirplane(airplaneId, request)
        }) {
            navigateBack()
        }
    }

//    private fun navigateToAirplaneDetails() {
//        navigateBack()
//        navController.navigate(AirplanesFragmentDirections.goToAirplaneDetails(airplaneId))
//    }


}