package pl.kossa.myflights.fragments.planned_flights.ofp

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.api.server.services.OFPsService
import pl.kossa.myflights.api.simbrief.models.OFP
import pl.kossa.myflights.api.simbrief.services.SimbriefService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class OFPDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val ofPsService: OFPsService,
    private val simbriefService: SimbriefService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    val timestamp = savedStateHandle.get<Long>("timestamp")!!
    val md5 = savedStateHandle.get<String>("md5")!!

    val ofpFlow = MutableStateFlow<OFP?>(null)

    fun fetchOFP() {
        makeRequest {
            val response = simbriefService.getOFP(timestamp, md5)
            response.body?.let {
                ofpFlow.value = it
                ofPsService.postOFP(it)
            }
        }
    }
}