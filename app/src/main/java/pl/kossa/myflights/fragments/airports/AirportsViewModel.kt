package pl.kossa.myflights.fragments.airports

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.BR
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Airport
import pl.kossa.myflights.api.services.AirportsService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class AirportsViewModel @Inject constructor(
    private val airportsService: AirportsService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    val airportsList = MutableLiveData<List<Airport>>()

    fun fetchAirports() {
        makeRequest(airportsService::getAllAirports) {
            airportsList.value = it
        }
    }


    @get:Bindable
    var noAirportsVisibility = false
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.noAirportsVisibility)
            }
        }


    fun deleteAirport(airportId: Int) {
        makeRequest({ airportsService.deleteAirort(airportId) }) {
            toastError.value = R.string.airport_deleted
            fetchAirports()
        }
    }

    fun navigateToAddAirport() {
        navDirectionLiveData.value = AirportsFragmentDirections.goToAirportAdd()
    }

    fun navigateToAirportDetails(airportId: Int) {
        navDirectionLiveData.value = AirportsFragmentDirections.goToAirportDetails(airportId)
    }
}