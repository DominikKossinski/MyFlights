package pl.kossa.myflights.fragments.airports.runways.add

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import pl.kossa.myflights.api.server.requests.RunwayRequest
import pl.kossa.myflights.api.server.services.RunwaysService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class RunwayAddViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val runwaysService: RunwaysService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val airportId = savedStateHandle.get<String>("airportId")!!

    private val _name = MutableStateFlow("")
    private val _length = MutableStateFlow<Int?>(0)
    private val _heading = MutableStateFlow<Int?>(0)
    private val _ilsFrequency = MutableStateFlow<String?>(null)
    val isAddButtonEnabled = combine(_name, _length, _heading) { name, length, heading ->
        if (heading == null || length == null) return@combine false
        //TODO display errors
        return@combine name.isNotBlank() && 1 <= length && length <= 5_000 && 0 <= heading && heading <= 360
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun setLength(length: Int?) {
        _length.value = length
    }

    fun setHeading(heading: Int?) {
        _heading.value = heading
    }

    fun setIlsFrequency(ilsFrequency: String?) {
        _ilsFrequency.value = ilsFrequency
    }

    fun postRunway() {
        // TODO add image
        val length = _length.value
        val heading = _heading.value
        if (length == null) {
            // TODO add error
            return
        }
        if (heading == null) {
            // TODO add error
            return
        }
        makeRequest {
            val response = runwaysService.postRunway(
                airportId,
                RunwayRequest(_name.value, length, heading, _ilsFrequency.value, null)
            )
            response.body?.let {
                navigate(RunwayAddFragmentDirections.goToRunwayDetails(airportId, it.entityId))
            }
        }
    }
}