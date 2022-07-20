package pl.kossa.myflights.fragments.airports.details

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.repository.AirportRepository
import pl.kossa.myflights.room.entities.Airport
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class AirportDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val airportRepository: AirportRepository,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val airportId = savedStateHandle.get<String>("airportId")!!
    val airport = MutableStateFlow<Airport?>(null)

    init {
        fetchAirport()
    }


    fun fetchAirport() {
        makeRequest {
            airport.value = handleRequest {
                airportRepository.getAirportById(airportId)
            }
        }
    }

    fun navigateToAirportEdit() {
        navigate(AirportDetailsFragmentDirections.goToAirportEdit(airportId))
    }

    fun deleteAirport() {
        makeRequest {
            val result = handleRequest {
                airportRepository.deleteAirport(airportId)
            }
            result?.let {
                analyticsTracker.logClickDeleteAirport()
                setToastMessage(R.string.airport_deleted)
                navigateBack()
            }
        }
    }

    fun navigateToRunwayAdd() {
        navigate(AirportDetailsFragmentDirections.goToRunwayAdd(airportId))
    }

    fun navigateToRunwayDetails(runwayId: String) {
        analyticsTracker.logClickRunwayDetails()
        navigate(AirportDetailsFragmentDirections.goToRunwayDetails(airportId, runwayId))
    }
}
