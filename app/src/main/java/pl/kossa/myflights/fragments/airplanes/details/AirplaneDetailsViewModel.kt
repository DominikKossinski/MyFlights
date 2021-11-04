package pl.kossa.myflights.fragments.airplanes.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
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
    val airplane = MutableStateFlow<Airplane?>(null)

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
        navigate(AirplaneDetailsFragmentDirections.goToAirplaneEdit(airplaneId))
    }

    fun deleteAirplane() {
        makeRequest {
            airplanesService.deleteAirplane(airplaneId)
            setToastMessage(R.string.airplane_deleted)
            navigateBack()
        }
    }
}
