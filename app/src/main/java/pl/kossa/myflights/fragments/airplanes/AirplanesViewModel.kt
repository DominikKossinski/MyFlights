package pl.kossa.myflights.fragments.airplanes

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
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

    val airplanesList = MutableLiveData<List<Airplane>>()

    fun fetchAirplanes() {
        makeRequest {
            val response = airplanesService.getAirplanes()
            response.body?.let { airplanesList.value = it }
        }
    }

    fun deleteAirplane(airplaneId: String) {
        makeRequest {
            airplanesService.getAirplaneById(airplaneId)
            setToastMessage(R.string.airplane_deleted)
            fetchAirplanes()
        }
    }

    fun navigateToAddAirplane() {
        navigate(MainFragmentDirections.goToAirplaneAdd())
    }

    fun navigateToAirplaneDetails(airplaneId: String) {
        navigate(MainFragmentDirections.goToAirplaneDetails(airplaneId))
    }
}
