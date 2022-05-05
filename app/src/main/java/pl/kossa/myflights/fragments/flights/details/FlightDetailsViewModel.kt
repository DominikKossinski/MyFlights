package pl.kossa.myflights.fragments.flights.details

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.flights.FlightResponse
import pl.kossa.myflights.api.services.FlightsService
import pl.kossa.myflights.api.services.SharedFlightsService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class FlightDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val flightsService: FlightsService,
    private val sharedFlightsService: SharedFlightsService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val flightId = savedStateHandle.get<String>("flightId")!!

    val flightResponse = MutableStateFlow<FlightResponse?>(null)

    fun fetchFlight() {
        makeRequest {
            val response = flightsService.getFLightById(flightId)
            response.body?.let { flightResponse.value = it }
        }
    }

    fun navigateToFlightEdit() {
        navigate(FlightDetailsFragmentDirections.goToFlightEdit(flightId))
    }

    fun deleteFlight() {
        makeRequest {
            flightsService.deleteFlight(flightId)
            analyticsTracker.logClickDeleteFlight()
            setToastMessage(R.string.flight_deleted)
            navigateBack()
        }
    }

    fun resignFromSharedFlight() {
        flightResponse.value?.let {
            it.sharedUsers.find { sharedUsers ->
                sharedUsers.userData?.userId == currentUser?.uid
            }?.let { shareData ->
                makeRequest {
                    sharedFlightsService.resignFromSharedFlight(shareData.sharedFlightId)
                    //TODO toast
                    analyticsTracker.logClickResignFromFlight()
                    navigateBack()
                }
            }
        }
    }

    fun navigateToFlightShareDialog() {
        analyticsTracker.logClickShareFlight()
        navigate(FlightDetailsFragmentDirections.goToShareFlightDialog(flightId))
    }

    fun navigateToSharedUsers() {
        analyticsTracker.logClickGoToSharedUsers()
        navigate(FlightDetailsFragmentDirections.goToSharedUsersFragment(flightId))
    }

    fun isMyFlight(userId: String): Boolean = userId == currentUser?.uid

}