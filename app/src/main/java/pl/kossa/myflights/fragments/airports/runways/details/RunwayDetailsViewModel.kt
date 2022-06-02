package pl.kossa.myflights.fragments.airports.runways.details

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.repository.RunwayRepository
import pl.kossa.myflights.room.entities.Runway
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class RunwayDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val runwayRepository: RunwayRepository,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val airportId = savedStateHandle.get<String>("airportId")!!
    private val runwayId = savedStateHandle.get<String>("runwayId")!!
    val runway = MutableStateFlow<Runway?>(null)

    init {
        fetchRunway()
    }

    fun fetchRunway() {
        makeRequest {
            runway.value = runwayRepository.getRunwayById(airportId, runwayId)
        }
    }

    fun navigateToRunwayEdit() {
        navigate(RunwayDetailsFragmentDirections.goToRunwayEdit(airportId, runwayId))
    }

    fun deleteRunway() {
        makeRequest {
            runwayRepository.deleteRunway(airportId, runwayId)
            analyticsTracker.logClickDeleteRunway()
            setToastMessage(R.string.runway_deleted)
            navigateBack()
        }
    }


}