package pl.kossa.myflights.fragments.airplanes

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import pl.kossa.myflights.BR
import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper

class AirplanesViewModel(navController: NavController, preferencesHelper: PreferencesHelper) :
    BaseViewModel(navController, preferencesHelper) {

    val airplanesList = MutableLiveData<List<Airplane>>()

    fun fetchAirplanes() {
        makeRequest(apiService.airplanesService::getAllAirplanes) {
            airplanesList.value = it
        }
    }

    @get:Bindable
    var noAirplanesVisibility = false
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.noAirplanesVisibility)
            }
        }
}
