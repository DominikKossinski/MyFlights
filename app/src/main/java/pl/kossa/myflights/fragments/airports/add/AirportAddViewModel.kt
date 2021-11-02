package pl.kossa.myflights.fragments.airports.add

import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import pl.kossa.myflights.api.requests.AirportRequest
import pl.kossa.myflights.api.services.AirportsService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.fragments.main.MainFragmentDirections
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class AirportAddViewModel @Inject constructor(
    private val airportsService: AirportsService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {


    private val _name = MutableStateFlow("")
    private val _city = MutableStateFlow("")
    private val _icaoCode = MutableStateFlow("")
    private val _towerFrequency = MutableStateFlow("")
    private val _groundFrequency = MutableStateFlow("")

    val isAddButtonEnabled = combine(_name, _city, _icaoCode) { name, city, icaoCode ->
        return@combine name.isNotBlank() && city.isNotBlank() && icaoCode.isNotBlank()
    }


    var nameError: Int? = null
        set(value) {
            if (field != value) {
                field = value
            }
        }


    var icaoShortcutError: Int? = null
        set(value) {
            if (field != value) {
                field = value
            }
        }


    var towerFrequencyError: Int? = null
        set(value) {
            if (field != value) {
                field = value
            }
        }


    var groundFrequencyError: Int? = null
        set(value) {
            if (field != value) {
                field = value
            }
        }


    fun postAirport() {
        val name = _name.value
        val city = _city.value
        val icaoCode = _icaoCode.value
        val towerFrequency = _towerFrequency.value
        val groundFrequency = _groundFrequency.value
        //TODO add data
        makeRequest {
            val request =
                AirportRequest(name, city, icaoCode, towerFrequency, groundFrequency, null)
            val response = airportsService.postAirport(request)
            response.body?.let { navigateToDetails(it.entityId) }
        }
    }

    private fun navigateToDetails(airportId: String) {
        navigate(AirportAddFragmentDirections.goToAirportDetails(airportId))
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
