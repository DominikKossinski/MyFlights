package pl.kossa.myflights.fragments.airplanes

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.BR
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.api.services.AirplanesService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class AirplanesViewModel @Inject constructor(
    private val airplanesService: AirplanesService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    val airplanesList = MutableLiveData<List<Airplane>>()

    fun fetchAirplanes() {
        makeRequest(airplanesService::getAllAirplanes) {
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
        makeRequest({ airplanesService.deleteAirplane(airplaneId) }) {
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
