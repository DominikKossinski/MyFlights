package pl.kossa.myflights.fragments.airplanes.edit

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import pl.kossa.myflights.api.requests.AirplaneRequest
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.architecture.ResultWrapper
import pl.kossa.myflights.repository.AirplaneRepository
import pl.kossa.myflights.room.entities.Airplane
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class AirplaneEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val airplaneRepository: AirplaneRepository,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val airplaneId = savedStateHandle.get<String>("airplaneId")!!
    private val _airplaneName = MutableStateFlow("")
    private val _maxSpeed = MutableStateFlow<Int?>(null)
    private val _weight = MutableStateFlow<Int?>(null)
    val airplane = MutableStateFlow<Airplane?>(null)

    val isSaveButtonEnabled = combine(_airplaneName, _maxSpeed, _weight) { name, speed, weight ->
        if (speed == null || weight == null) return@combine false
        return@combine name.isNotBlank()
                && 1 <= speed && speed <= 500
                && 1 <= weight && weight <= 500
                && !isLoadingData.value
    }

    internal fun setAirplaneName(name: String) {
        _airplaneName.value = name
    }

    internal fun setMaxSpeed(maxSpeed: Int?) {
        _maxSpeed.value = maxSpeed
    }

    internal fun setWeight(weight: Int?) {
        _weight.value = weight
    }

    private fun fetchAirplane() {
        makeRequest {
            val result = airplaneRepository.getAirplaneById(airplaneId)
            airplane.value = result.value
            if (result is ResultWrapper.GenericError) {
                apiErrorFlow.emit(result.apiError)
            }
            if(result is ResultWrapper.NetworkError) {
                networkErrorFlow.emit(result.networkErrorType)
            }
        }
    }

    init {
        fetchAirplane()
    }


    fun putAirplane() {
        makeRequest {
            //TODO image
            val request = AirplaneRequest(
                _airplaneName.value,
                _maxSpeed.value,
                _weight.value,
                null
            )
            airplaneRepository.saveAirplane(airplaneId, request)
            navigateBack()
        }
    }

}