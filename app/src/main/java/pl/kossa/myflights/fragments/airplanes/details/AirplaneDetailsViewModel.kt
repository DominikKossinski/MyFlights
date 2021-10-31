package pl.kossa.myflights.fragments.airplanes.details

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.ResponseBody
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.api.responses.ApiErrorBody
import pl.kossa.myflights.api.services.AirplanesService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import retrofit2.Converter
import javax.inject.Inject

@HiltViewModel
class AirplaneDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val airplanesService: AirplanesService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val airplaneId = savedStateHandle.get<String>("airplaneId")!!
    val airplane = MutableLiveData<Airplane>()

    init {
        fetchAirplane()
    }

    fun fetchAirplane() {
        makeRequest {
            val response = airplanesService.getAirplaneById(airplaneId)
            response.body?.let { airplane.value = it }
        }
    }

    fun navigateToAirplaneEdit() {
        navDirectionLiveData.postValue(AirplaneDetailsFragmentDirections.goToAirplaneEdit(airplaneId))
    }

    fun deleteAirplane() {
        makeRequest {
            airplanesService.deleteAirplane(airplaneId)
            setToastMessage(R.string.airplane_deleted)
        }
    }
}
