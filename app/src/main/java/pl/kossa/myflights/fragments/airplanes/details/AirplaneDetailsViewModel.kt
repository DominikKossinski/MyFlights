package pl.kossa.myflights.fragments.airplanes.details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.api.services.AirplanesService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class AirplaneDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val airplanesService: AirplanesService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val airplaneId = savedStateHandle.get<String>("airplaneId")!!
    val airplaneLiveData = MutableLiveData<Airplane>()

    init {
        fetchAirplane()
    }

    fun fetchAirplane() {
        makeRequest({
            Log.d("MyLog", "AirplaneId: $airplaneId")
            airplanesService.getAirplaneById(airplaneId)
        }) { it ->
            airplaneLiveData.value = it
        }
    }

    fun navigateToAirplaneEdit() {
        navDirectionLiveData.postValue(AirplaneDetailsFragmentDirections.goToAirplaneEdit(airplaneId))
    }

    fun deleteAirplane() {
        makeRequest({
            airplanesService.deleteAirplane(airplaneId)
        }
        ) {
            toastError.value = R.string.airplane_deleted
        }
    }
}
