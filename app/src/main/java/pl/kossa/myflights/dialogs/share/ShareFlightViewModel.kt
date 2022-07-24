package pl.kossa.myflights.dialogs.share

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import pl.kossa.myflights.api.models.SharedFlight
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.repository.SharedFlightRepository
import pl.kossa.myflights.utils.PreferencesHelper
import pl.kossa.myflights.utils.links.DynamicLinksResolver
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ShareFlightViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sharedFlightRepository: SharedFlightRepository,
    private val dynamicLinksResolver: DynamicLinksResolver,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {


    private val flightId = savedStateHandle.get<String>("flightId")!!

    private val sharedFlightFlow = MutableStateFlow<SharedFlight?>(null)
    private val sharedFlightDynamicLink = sharedFlightFlow.flatMapLatest {
        it?.sharedFlightId?.let { sharedFlightId ->
            dynamicLinksResolver.getSharedFlightDynamicLink(sharedFlightId)
        } ?: flowOf(null)
    }
    val sharedFlightWithLink =
        sharedFlightFlow.combine(sharedFlightDynamicLink) { sharedFlight, link ->
            return@combine Pair(sharedFlight, link)
        }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    init {
        fetchSharedFlight()
    }

    fun fetchSharedFlight() {
        makeRequest {
            val response = handleRequest {
                sharedFlightRepository.shareFlight(flightId)
            }
            sharedFlightFlow.value = response
        }
    }

    fun logClickShareWithLink() {
        analyticsTracker.logClickShareWithLink()
    }

}