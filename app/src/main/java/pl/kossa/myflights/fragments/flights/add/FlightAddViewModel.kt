package pl.kossa.myflights.fragments.flights.add

import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.fragments.flights.select.runway.RunwaySelectFragment
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class FlightAddViewModel @Inject constructor(
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    val airplaneName = MutableStateFlow("")
    val airplaneId = MutableStateFlow("")

    val departureAirportId = MutableStateFlow("")
    val departureAirportName = MutableStateFlow("")

    val departureRunwayId = MutableStateFlow("")
    val departureRunwayName = MutableStateFlow("")

    val arrivalAirportId = MutableStateFlow("")
    val arrivalAirportName = MutableStateFlow("")

    val arrivalRunwayId = MutableStateFlow("")
    val arrivalRunwayName = MutableStateFlow("")

    fun navigateToAirplaneSelect() {
        navDirectionLiveData.value = FlightAddFragmentDirections.goToAirplaneSelect()
    }

    fun navigateToAirportSelect(key: String) {
        navDirectionLiveData.value = FlightAddFragmentDirections.goToAirportSelect(key)
    }

    fun navigateToRunwaySelect(key: String) {
        val airportId = when (key) {
            RunwaySelectFragment.ARRIVAL_RUNWAY_KEY -> {
                arrivalAirportId.value
            }
            RunwaySelectFragment.DEPARTURE_RUNWAY_KEY -> {
                departureAirportId.value
            }
            else -> null
        }
        navDirectionLiveData.value =
            airportId?.let { FlightAddFragmentDirections.goToRunwaySelect(key, airportId) }
    }
}