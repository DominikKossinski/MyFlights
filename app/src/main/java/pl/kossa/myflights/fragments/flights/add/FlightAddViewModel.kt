package pl.kossa.myflights.fragments.flights.add

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import pl.kossa.myflights.api.requests.FlightRequest
import pl.kossa.myflights.api.services.FlightsService
import pl.kossa.myflights.api.services.SharedFlightsService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.fragments.flights.select.runway.RunwaySelectFragment
import pl.kossa.myflights.utils.PreferencesHelper
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FlightAddViewModel @Inject constructor(
    private val flightsService: FlightsService,
    private val sharedFlightsService: SharedFlightsService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val _airplaneId = MutableStateFlow("")
    val _airplaneName = MutableStateFlow("")

    private val _departureAirportId = MutableStateFlow("")
    val _departureAirportName = MutableStateFlow("")

    private val _departureRunwayId = MutableStateFlow("")
    val _departureRunwayName = MutableStateFlow("")

    private val _arrivalAirportId = MutableStateFlow("")
    val _arrivalAirportName = MutableStateFlow("")

    private val _arrivalRunwayId = MutableStateFlow("")
    val _arrivalRunwayName = MutableStateFlow("")

    val _departureDate = MutableStateFlow<Date?>(null)
    val _arrivalDate = MutableStateFlow<Date?>(null)

    private val _distance = MutableStateFlow<Int?>(null)
    private val _note = MutableStateFlow("")

    private val _airplaneAndAirportsOk = combine(
        _airplaneId,
        _departureAirportId,
        _departureRunwayId,
        _arrivalAirportId,
        _arrivalRunwayId
    ) { airplaneId, departureAirportId, departureRunwayId, arrivalAirportId, arrivalRunwayId ->
        return@combine airplaneId.isNotBlank() && departureAirportId.isNotBlank() &&
                departureRunwayId.isNotBlank() && arrivalAirportId.isNotBlank() &&
                arrivalRunwayId.isNotBlank()
    }
    val isAddButtonEnabled = combine(
        _airplaneAndAirportsOk,
        _departureDate,
        _arrivalDate
    ) { airplaneAndAirportsOk, departureDate, arrivalDate ->
        return@combine airplaneAndAirportsOk && departureDate != null && arrivalDate != null && departureDate.time <= arrivalDate.time
    }

    fun postFlight() {
        val departureDate = _departureDate.value
        val arrivalDate = _arrivalDate.value
        if (departureDate == null || arrivalDate == null) return // TODO diplay error
        makeRequest {
            val response = flightsService.postFlight(
                FlightRequest(
                    _note.value,
                    _distance.value,
                    null, // TODO image
                    departureDate,
                    arrivalDate,
                    _airplaneId.value,
                    _departureAirportId.value,
                    _departureRunwayId.value,
                    _arrivalAirportId.value,
                    _arrivalRunwayId.value
                )
            )
            analyticsTracker.logClickAddFlight()
            response.body?.let { navigateToFlightDetails(it.entityId) }
        }
    }

    private fun navigateToFlightDetails(flightId: String) {
        navigate(FlightAddFragmentDirections.goToFlightDetails(flightId))
    }

    fun navigateToAirplaneSelect() {
        navigate(FlightAddFragmentDirections.goToAirplaneSelect())
    }

    fun navigateToAirportSelect(key: String) {
        navigate(FlightAddFragmentDirections.goToAirportSelect(key))
    }

    fun navigateToRunwaySelect(key: String) {
        val airportId = when (key) {
            RunwaySelectFragment.ARRIVAL_RUNWAY_KEY -> {
                _arrivalAirportId.value
            }
            RunwaySelectFragment.DEPARTURE_RUNWAY_KEY -> {
                _departureAirportId.value
            }
            else -> null
        }
        airportId?.let { navigate(FlightAddFragmentDirections.goToRunwaySelect(key, airportId)) }
    }

    fun setAirplaneId(airplaneId: String) {
        _airplaneId.value = airplaneId
    }

    fun setDepartureAirportId(airportId: String) {
        _departureAirportId.value = airportId
    }

    fun setDepartureRunwayId(runwayId: String) {
        _departureRunwayId.value = runwayId
    }

    fun setArrivalAirportId(airportId: String) {
        _arrivalAirportId.value = airportId
    }

    fun setArrivalRunwayId(runwayId: String) {
        _arrivalRunwayId.value = runwayId
    }

    fun setNote(note: String) {
        _note.value = note
    }

    fun setDistance(distance: Int?) {
        _distance.value = distance
    }

    fun navigateToScanQRCodeFragment() {
        analyticsTracker.logClickScanQRCode()
        navigate(FlightAddFragmentDirections.goToScanQRCodeFragment())
    }
}