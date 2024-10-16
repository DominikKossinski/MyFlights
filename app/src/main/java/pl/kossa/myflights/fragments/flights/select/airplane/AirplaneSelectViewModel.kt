package pl.kossa.myflights.fragments.flights.select.airplane

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.repository.AirplaneRepository
import pl.kossa.myflights.room.entities.Airplane
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class AirplaneSelectViewModel @Inject constructor(
    private val airplaneRepository: AirplaneRepository,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    val airplanesList = MutableStateFlow<List<Airplane>>(emptyList())

    init {
        fetchAirplanes("")
    }

    fun fetchAirplanes(text: String) {
        makeRequest {
            val result = handleRequest {
                airplaneRepository.getAirplanes(text)
            }
            airplanesList.value = result
//         TODO   response.body?.let {
//                airplanesList.value = it
//            }
        }
    }

}