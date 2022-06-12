package pl.kossa.myflights.dialogs.share

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.api.models.SharedFlight
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.repository.SharedFlightRepository
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class ShareFlightViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sharedFlightRepository: SharedFlightRepository,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {


    private val flightId = savedStateHandle.get<String>("flightId")!!

    val sharedFlightFlow = MutableStateFlow<SharedFlight?>(null)

    init {
        fetchSharedFlight()
    }

    fun fetchSharedFlight() {
        makeRequest {
            val response = sharedFlightRepository.shareFlight(flightId)
            sharedFlightFlow.value = response.body
        }
    }

    fun logClickShareWithLink() {
        analyticsTracker.logClickShareWithLink()
    }

}