package pl.kossa.myflights.fragments.airports.details

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.R
import pl.kossa.myflights.api.ApiService
import pl.kossa.myflights.api.models.Airport
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class AirportDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val apiService: ApiService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val airportId = savedStateHandle.get<Int>("airportId")!!

    init {
        fetchAirport()
    }

    val airportLiveData = MutableLiveData<Airport>()

    var airport: Airport? = null
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    val airportName
        get() = airport?.name ?: ""

    @get:Bindable
    val city
        get() = airport?.city ?: ""

    fun fetchAirport() {
        makeRequest({
            apiService.airportsService.getAirportById(airportId)
        }) { it ->
            airportLiveData.value = it
        }
    }

    fun navigateToAirportEdit() {
    navDirectionLiveData.value =  AirportDetailsFragmentDirections.goToAirportEdit(airportId)
    }

    fun deleteAirport() {
        makeRequest({
            apiService.airportsService.deleteAirort(airportId)
        }
        ) {
            toastError.value = R.string.airport_deleted
            navigateBack()
        }
    }
}
