package pl.kossa.myflights.fragments.airports.edit

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import pl.kossa.myflights.api.requests.AirportRequest
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.repository.AirportRepository
import pl.kossa.myflights.room.entities.Airport
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class AirportEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val airportRepository: AirportRepository,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val airportId = savedStateHandle.get<String>("airportId")!!

    private val _name = MutableStateFlow("")
    private val _city = MutableStateFlow("")
    private val _icaoCode = MutableStateFlow("")
    private val _towerFrequency = MutableStateFlow("")
    private val _groundFrequency = MutableStateFlow("")

    val airport = MutableStateFlow<Airport?>(null)


    val isSaveButtonEnabled = combine(_name, _city, _icaoCode) { name, city, icaoCode ->
        return@combine name.isNotBlank() && city.isNotBlank() && icaoCode.isNotBlank()
    }


    init {
        fetchAirport()
    }

    private fun fetchAirport() {
        makeRequest {
            airport.value = handleRequest {
                airportRepository.getAirportById(airportId)
            }
        }
    }

    fun putAirport() {
        val name = _name.value
        val city = _city.value
        val icaoCode = _icaoCode.value
        val towerFrequency = _towerFrequency.value
        val groundFrequency = _groundFrequency.value

        makeRequest {
            //TODO image and data
            val request =
                AirportRequest(name, city, icaoCode, towerFrequency, groundFrequency, null)
            airportRepository.saveAirport(airportId, request)
            navigateBack()
        }
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun setCity(city: String) {
        _city.value = city
    }

    fun setIcaoCode(icaoCode: String) {
        _icaoCode.value = icaoCode
    }

    fun setTowerFrequency(towerFrequency: String) {
        _towerFrequency.value = towerFrequency
    }

    fun setGroundFrequency(groundFrequency: String) {
        _groundFrequency.value = groundFrequency
    }


}