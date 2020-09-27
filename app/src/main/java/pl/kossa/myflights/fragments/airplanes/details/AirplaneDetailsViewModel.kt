package pl.kossa.myflights.fragments.airplanes.details

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper

class AirplaneDetailsViewModel(
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
            notifyChange()
        }

    @get:Bindable
    val airplaneName
        get() = airplane?.name ?: ""

    fun fetchAirplane() {
        makeRequest({
            apiService.airplanesService.getAirplaneById(airplaneId)
        }) { it ->
            airplaneLiveData.value = it
        }
    }

    fun navigateToAirplaneEdit() {
        navController.navigate(AirplaneDetailsFragmentDirections.goToAirplaneEdit(airplaneId))
    }

    fun deleteAirplane() {
        makeRequest({
            apiService.airplanesService.deleteAirplane(airplaneId)
        }
        ) {
            toastError.value = R.string.airplane_deleted
            navigateBack()
        }
    }
}
