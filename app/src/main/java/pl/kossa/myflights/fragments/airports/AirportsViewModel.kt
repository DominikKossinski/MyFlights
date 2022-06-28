package pl.kossa.myflights.fragments.airports

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import pl.kossa.myflights.R
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.fragments.main.MainFragmentDirections
import pl.kossa.myflights.repository.AirportRepository
import pl.kossa.myflights.room.entities.Airport
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class AirportsViewModel @Inject constructor(
    private val airportRepository: AirportRepository,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    val airportsList = MutableStateFlow<List<Airport>>(emptyList())

    fun fetchAirports() {
        makeRequest {
            airportsList.value = handleRequest {
                airportRepository.getAirports()
            }
        }
    }

    fun deleteAirport(airportId: String) {
        makeRequest {
            val result = handleRequest {
                airportRepository.deleteAirport(airportId)
            }
            result?.let {
                analyticsTracker.logClickDeleteAirport()
                setToastMessage(R.string.airport_deleted)
                fetchAirports()
            }
        }
    }

    fun navigateToAddAirport() {
        navigate(MainFragmentDirections.goToAirportAdd())
    }

    fun navigateToAirportDetails(airportId: String) {
        analyticsTracker.logClickAirportDetails()
        navigate(MainFragmentDirections.goToAirportDetails(airportId))
    }
}