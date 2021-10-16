package pl.kossa.myflights.fragments.airports

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.ResponseBody
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Airport
import pl.kossa.myflights.api.responses.ApiErrorBody
import pl.kossa.myflights.api.services.AirportsService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.fragments.main.MainFragmentDirections
import pl.kossa.myflights.utils.PreferencesHelper
import retrofit2.Converter
import javax.inject.Inject

@HiltViewModel
class AirportsViewModel @Inject constructor(
    private val airportsService: AirportsService,
    errorBodyConverter: Converter<ResponseBody, ApiErrorBody>,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(errorBodyConverter, preferencesHelper) {

    val airportsList = MutableLiveData<List<Airport>>()

    fun fetchAirports() {
        makeRequest(airportsService::getAirports) {
            airportsList.value = it
        }
    }

    fun deleteAirport(airportId: String) {
        makeRequest({ airportsService.deleteAirport(airportId) }) {
            setToastError(R.string.airport_deleted)
            fetchAirports()
        }
    }

    fun navigateToAddAirport() {
        navDirectionLiveData.value = MainFragmentDirections.goToAirportAdd()
    }

    fun navigateToAirportDetails(airportId: String) {
        navDirectionLiveData.value = MainFragmentDirections.goToAirportDetails(airportId)
    }
}