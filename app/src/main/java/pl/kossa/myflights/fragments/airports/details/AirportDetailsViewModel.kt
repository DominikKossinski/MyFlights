package pl.kossa.myflights.fragments.airports.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Airport
import pl.kossa.myflights.api.services.AirportsService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class AirportDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val airportsService: AirportsService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val airportId = savedStateHandle.get<String>("airportId")!!

    init {
        fetchAirport()
    }

    val airportLiveData = MutableLiveData<Airport>()

    fun fetchAirport() {
        makeRequest({
            airportsService.getAirportById(airportId)
        }) { it ->
            airportLiveData.value = it
        }
    }

    fun navigateToAirportEdit() {
        navDirectionLiveData.value = AirportDetailsFragmentDirections.goToAirportEdit(airportId)
    }

    fun deleteAirport() {
        makeRequest({
            airportsService.deleteAirport(airportId)
        }
        ) {
            toastError.value = R.string.airport_deleted
            navigateBack()
        }
    }

    fun navigateToRunwayAdd() {
        navDirectionLiveData.value = AirportDetailsFragmentDirections.goToRunwayAdd(airportId)
    }

    fun navigateToRunwayDetails(runwayId: String) {
        navDirectionLiveData.value =
            AirportDetailsFragmentDirections.goToRunwayDetails(airportId, runwayId)
    }
}
