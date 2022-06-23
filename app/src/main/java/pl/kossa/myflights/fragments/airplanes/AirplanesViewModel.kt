package pl.kossa.myflights.fragments.airplanes

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.fragments.main.MainFragmentDirections
import pl.kossa.myflights.repository.AirplaneRepository
import pl.kossa.myflights.room.entities.Airplane
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class AirplanesViewModel @Inject constructor(
    private val airplaneRepository: AirplaneRepository,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    val airplanesList = MutableStateFlow<List<Airplane>>(emptyList())

    fun fetchAirplanes() {
        makeRequest {
            airplanesList.value = handleRequest {
                airplaneRepository.getAirplanes()
            }
        }
    }

    fun deleteAirplane(airplaneId: String) {
        makeRequest {
            val result = handleRequest {
                airplaneRepository.deleteAirplane(airplaneId)
            }
            result?.let {
                analyticsTracker.logClickDeleteAirplane()
                setToastMessage(R.string.airplane_deleted)
                fetchAirplanes()
            }
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
