package pl.kossa.myflights.fragments.airports.runways.details

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.ResponseBody
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Runway
import pl.kossa.myflights.api.responses.ApiErrorBody
import pl.kossa.myflights.api.services.RunwaysService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import retrofit2.Converter
import javax.inject.Inject

@HiltViewModel
class RunwayDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val runwaysService: RunwaysService,
    errorBodyConverter: Converter<ResponseBody, ApiErrorBody>,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(errorBodyConverter, preferencesHelper) {

    private val airportId = savedStateHandle.get<String>("airportId")!!
    private val runwayId = savedStateHandle.get<String>("runwayId")!!
    val runway = MutableStateFlow<Runway?>(null)

    init {
        fetchRunway()
    }

    fun fetchRunway() {
        makeRequest({ runwaysService.getRunwayById(airportId, runwayId) }) { response ->
            runway.value = response
        }
    }

    fun navigateToRunwayEdit() {
        navDirectionLiveData.value =
            RunwayDetailsFragmentDirections.goToRunwayEdit(airportId, runwayId)
    }

    fun deleteRunway() {
        makeRequest({
            runwaysService.deleteRunway(airportId, runwayId)
        }) {
            setToastError(R.string.runway_deleted)
            navigateBack()
        }
    }


}