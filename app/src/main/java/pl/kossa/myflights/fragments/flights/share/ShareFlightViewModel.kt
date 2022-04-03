package pl.kossa.myflights.fragments.flights.share

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.api.services.SharedFlightsService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class ShareFlightViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sharedFlightsService: SharedFlightsService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val flightId = savedStateHandle.get<String>("flightId")!!

    val sharedFlightId = MutableStateFlow<String?>(null)

    init {
        makeRequest {
            val response = sharedFlightsService.shareFlight(flightId)
            sharedFlightId.value = response.body?.entityId
        }
    }

}