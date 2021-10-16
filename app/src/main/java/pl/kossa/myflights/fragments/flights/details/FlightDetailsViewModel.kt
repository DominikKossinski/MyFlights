package pl.kossa.myflights.fragments.flights.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.ResponseBody
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Flight
import pl.kossa.myflights.api.responses.ApiErrorBody
import pl.kossa.myflights.api.services.FlightsService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import retrofit2.Converter
import javax.inject.Inject

@HiltViewModel
class FlightDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val flightsService: FlightsService,
    errorBodyConverter: Converter<ResponseBody, ApiErrorBody>,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(errorBodyConverter, preferencesHelper) {

    private val flightId = savedStateHandle.get<String>("flightId")!!

    val flight = MutableStateFlow<Flight?>(null)

    fun fetchFlight() {
        makeRequest({
            flightsService.getFLightById(flightId)
        }) { it ->
            flight.value = it
        }
    }

    fun navigateToFlightEdit() {
        navDirectionLiveData.value = FlightDetailsFragmentDirections.goToFlightEdit(flightId)
    }

    fun deleteFlight() {
        makeRequest({ flightsService.deleteFlight(flightId) }) {
            setToastError(R.string.flight_deleted)
            navigateBack()
        }
    }

}