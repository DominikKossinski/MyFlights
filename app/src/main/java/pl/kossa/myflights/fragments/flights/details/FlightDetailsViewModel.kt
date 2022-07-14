package pl.kossa.myflights.fragments.flights.details

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.repository.FlightRepository
import pl.kossa.myflights.repository.SharedFlightRepository
import pl.kossa.myflights.room.entities.Flight
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class FlightDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val flightRepository: FlightRepository,
    private val sharedFlightRepository: SharedFlightRepository,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val flightId = savedStateHandle.get<String>("flightId")!!

    val flight = MutableStateFlow<Flight?>(null)

    fun fetchFlight() {
        makeRequest {
            flight.value = handleRequest {
                flightRepository.getFlightById(flightId)
            }
        }
    }

    fun navigateToFlightEdit() {
        navigate(FlightDetailsFragmentDirections.goToFlightEdit(flightId))
    }

    fun deleteFlight() {
        makeRequest {
            val result = handleRequest {
                flightRepository.deleteFlight(flightId)
            }
            result?.let {
                analyticsTracker.logClickDeleteFlight()
                setToastMessage(R.string.flight_deleted)
                navigateBack()
            }
        }
    }

    fun resignFromSharedFlight() {
        flight.value?.let {
            it.sharedUsers.find { sharedUsers ->
                sharedUsers.sharedData.sharedUserId == currentUser?.uid
            }?.let { shareData ->
                makeRequest {
                    val result = handleRequest {
                        sharedFlightRepository.resignFromSharedFlight(shareData.sharedData.sharedFlightId)
                    }
                    result?.let {
                        //TODO toast
                        analyticsTracker.logClickResignFromFlight()
                        navigateBack()
                    }
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