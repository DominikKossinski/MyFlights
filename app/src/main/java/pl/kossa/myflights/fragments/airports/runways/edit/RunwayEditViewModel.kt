package pl.kossa.myflights.fragments.airports.runways.edit

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import pl.kossa.myflights.room.entities.Runway
import pl.kossa.myflights.api.requests.RunwayRequest
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.repository.RunwayRepository
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class RunwayEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val runwayRepository: RunwayRepository,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val airportId = savedStateHandle.get<String>("airportId")!!
    private val runwayId = savedStateHandle.get<String>("runwayId")!!
    val runway = MutableStateFlow<Runway?>(null)

    private val _name = MutableStateFlow("")
    private val _length = MutableStateFlow<Int?>(null)
    private val _heading = MutableStateFlow<Int?>(null)
    private val _ilsFrequency = MutableStateFlow<String?>(null)
    val isSaveButtonEnabled = combine(_name, _length, _heading) { name, length, heading ->
        if (heading == null || length == null) return@combine false
        return@combine name.isNotBlank() && 1 <= length && length <= 5_000 && 0 <= heading && heading <= 360
    }

    init {
        fetchRunway()
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


    fun fetchRunway() {
        makeRequest {
            runway.value = runwayRepository.getRunwayById(airportId, runwayId)
        }
    }

    fun putRunway() {
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
            runwayRepository.savaRunway(
                airportId, runwayId,
                RunwayRequest(_name.value, length, heading, _ilsFrequency.value, null)
            )
            navigateBack()
        }
    }
}