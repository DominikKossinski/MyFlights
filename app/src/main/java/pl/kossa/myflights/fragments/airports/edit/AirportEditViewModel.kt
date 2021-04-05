package pl.kossa.myflights.fragments.airports.edit

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.BR
import pl.kossa.myflights.api.models.Airport
import pl.kossa.myflights.api.requests.AirportRequest
import pl.kossa.myflights.api.services.AirportsService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class AirportEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val airportsService: AirportsService,
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
            if (value != null) {
                name = value.name
                //TODO set values
            }
            notifyChange()
        }

    @get:Bindable
    var name = ""
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.airplaneName)
            }
        }

    @get:Bindable
    var nameError: Int? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.nameError)
            }
        }


    private fun fetchAirport() {
        makeRequest({
            airportsService.getAirportById(airportId)
        }) { it ->
            airportLiveData.value = it
        }
    }

    fun saveAirport() {
        makeRequest({
            //TODO image and data
            val request = AirportRequest(name, "", "", "", "", null)
            airportsService.putAirport(airportId, request)
        }) {
            navigateBack()
        }
    }


}