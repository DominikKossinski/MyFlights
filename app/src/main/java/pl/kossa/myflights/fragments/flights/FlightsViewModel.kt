package pl.kossa.myflights.fragments.flights

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.ResponseBody
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Flight
import pl.kossa.myflights.api.responses.ApiErrorBody
import pl.kossa.myflights.api.services.FlightsService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.fragments.main.MainFragmentDirections
import pl.kossa.myflights.utils.PreferencesHelper
import retrofit2.Converter
import javax.inject.Inject

@HiltViewModel
class FlightsViewModel @Inject constructor(
    private val flightsService: FlightsService,
    errorBodyConverter: Converter<ResponseBody, ApiErrorBody>,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(errorBodyConverter, preferencesHelper) {


    val flightsList = MutableStateFlow<List<Flight>>(emptyList())

    init {
        fetchFlights()
    }

    fun fetchFlights() {
        makeRequest(flightsService::getAllFlights) {
            flightsList.value = it
        }
    }

    fun navigateToAddFlight() {
        navDirectionLiveData.value = MainFragmentDirections.goToFlightAdd()
    }

    fun navigateToFlightDetails(flightId: String) {
        navDirectionLiveData.value = MainFragmentDirections.goToFlightDetails(flightId)
    }

    fun deleteFlight(flightId: String) {
        makeRequest({flightsService.deleteFlight(flightId)}) {
            setToastError(R.string.flight_deleted)
            fetchFlights()
        }
    }


}