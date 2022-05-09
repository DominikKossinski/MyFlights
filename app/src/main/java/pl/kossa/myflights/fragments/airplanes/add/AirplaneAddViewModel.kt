package pl.kossa.myflights.fragments.airplanes.add

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import pl.kossa.myflights.R
import pl.kossa.myflights.api.requests.AirplaneRequest
import pl.kossa.myflights.api.services.AirplanesService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.fragments.main.MainFragmentDirections
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class AirplaneAddViewModel @Inject constructor(
    private val airplanesService: AirplanesService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val _airplaneName = MutableStateFlow("")
    private val _maxSpeed = MutableStateFlow<Int?>(null)
    private val _weight = MutableStateFlow<Int?>(null)

    val isAddButtonEnabled = combine(_airplaneName, _maxSpeed, _weight) { name, speed, weight ->
        if (speed == null || weight == null) return@combine false
        return@combine name.isNotBlank()
                && 1 <= speed && speed <= 500
                && 1 <= weight && weight <= 500
                && !isLoadingData.value
    }

    val nameError = MutableStateFlow<Int?>(null)

    internal fun setAirplaneName(name: String) {
        _airplaneName.value = name
    }

    internal fun setMaxSpeed(maxSpeed: Int?) {
        _maxSpeed.value = maxSpeed
    }

    internal fun setWeight(weight: Int?) {
        _weight.value = weight
    }

    fun postAirplane() {
        nameError.value = null
        val name = _airplaneName.value
        val maxSpeed = _maxSpeed.value
        val weight = _weight.value
        if (name.isBlank()) {
            nameError.value = R.string.error_empty_name
            return
        }
        makeRequest {
            //TODO image
            val request = AirplaneRequest(name, maxSpeed, weight, null)
            val response = airplanesService.postAirplane(request)
            analyticsTracker.logClickAddAirplane()
            response.body?.let {
                navigateToDetails(it.entityId)
            }
        }
    }

    private fun navigateToDetails(airplaneId: String) {
        navigate(AirplaneAddFragmentDirections.goToAirplaneDetails(airplaneId))
    }
}
