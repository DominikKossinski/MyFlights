package pl.kossa.myflights.fragments.airports.runways.add

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import pl.kossa.myflights.api.requests.RunwayRequest
import pl.kossa.myflights.api.services.RunwaysService
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
    private val _length = MutableStateFlow(0)
    private val _heading = MutableStateFlow(0)
    private val _ilsFrequency = MutableStateFlow<String?>(null)
    val isAddButtonEnabled = combine(_name, _length, _heading) { name, length, heading ->
        return@combine name.isNotBlank() && 1 <= length && length <= 5_000 && 0 <= heading && heading <= 360
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun setLength(length: Int) {
        _length.value = length
    }

    fun setHeading(heading: Int) {
        _heading.value = heading
    }

    fun setIlsFrequency(ilsFrequency: String?) {
        _ilsFrequency.value = ilsFrequency
    }

    fun postRunway() {
        // TODO add image
        makeRequest({
            runwaysService.postRunway(
                airportId,
                RunwayRequest(_name.value, _length.value, _heading.value, _ilsFrequency.value, null)
            )
        }) { repsonse ->
            navigateBack()
            // TODO navigate to runways details
        }
    }
}