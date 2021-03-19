package pl.kossa.myflights.fragments.airplanes

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.BR
import pl.kossa.myflights.R
import pl.kossa.myflights.api.ApiService
import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class AirplanesViewModel @Inject constructor(
    private val apiService: ApiService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

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

    fun deleteAirplane(airplaneId: Int) {
        makeRequest({
            apiService.airplanesService.deleteAirplane(airplaneId)
        }
        ) {
            toastError.value = R.string.airplane_deleted
            fetchAirplanes()
        }
    }

    fun navigateToAddAirplane() {
        navDirectionLiveData.value = AirplanesFragmentDirections.goToAirplaneAdd()
    }

    fun navigateToAirplaneDetails(airplaneId: Int) {
        navDirectionLiveData.value = AirplanesFragmentDirections.goToAirplaneDetails(airplaneId)
    }
}
