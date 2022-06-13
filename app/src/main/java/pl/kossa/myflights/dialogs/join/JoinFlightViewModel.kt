package pl.kossa.myflights.dialogs.join

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.api.responses.sharedflights.SharedFlightJoinDetails
import pl.kossa.myflights.api.services.SharedFlightsService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.repository.SharedFlightRepository
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class JoinFlightViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sharedFlightRepository: SharedFlightRepository,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val sharedFlightId = savedStateHandle.get<String>("sharedFlightId")!!

    val sharedFlightJoinDetails = MutableStateFlow<SharedFlightJoinDetails?>(null)

    init {
        fetchSharedFlightJoinDetails()
    }

    private fun fetchSharedFlightJoinDetails() {
        makeRequest {
            val response = sharedFlightRepository.getSharedFlightJoinDetails(sharedFlightId)
            analyticsTracker.logJoinRequestScanned()
            response.body?.let { sharedFlightJoinDetails.value = it }
        }
    }

    fun joinSharedFlight() {
        makeRequest {
            sharedFlightRepository.joinSharedFlight(sharedFlightId)
            analyticsTracker.logClickJoinFlight()
            navigate(JoinFlightBottomSheetDirections.showJoinRequestSentDialog())
        }
    }
}