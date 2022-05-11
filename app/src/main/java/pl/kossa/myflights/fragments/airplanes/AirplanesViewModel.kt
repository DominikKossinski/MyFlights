package pl.kossa.myflights.fragments.airplanes

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.api.services.AirplanesService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.fragments.main.MainFragmentDirections
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class AirplanesViewModel @Inject constructor(
    private val airplanesService: AirplanesService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    val airplanesList = MutableStateFlow<List<Airplane>>(emptyList())

    fun fetchAirplanes() {
        makeRequest {
            val response = airplanesService.getAirplanes()
            response.body?.let { airplanesList.value = it }
        }
    }

    fun deleteAirplane(airplaneId: String) {
        makeRequest {
            airplanesService.deleteAirplane(airplaneId)
            analyticsTracker.logClickDeleteAirplane()
            setToastMessage(R.string.airplane_deleted)
            fetchAirplanes()
        }
    }

    fun navigateToAddAirplane() {
        navigate(MainFragmentDirections.goToAirplaneAdd())
    }

    fun navigateToAirplaneDetails(airplaneId: String) {
        analyticsTracker.logClickAirplaneDetails()
        navigate(MainFragmentDirections.goToAirplaneDetails(airplaneId))
    }
}
