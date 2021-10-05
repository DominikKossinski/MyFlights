package pl.kossa.myflights.fragments.airplanes.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.api.requests.AirplaneRequest
import pl.kossa.myflights.api.services.AirplanesService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.fragments.main.MainFragmentDirections
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class AirplaneEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val airplanesService: AirplanesService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    private val airplaneId = savedStateHandle.get<String>("airplaneId")!!

    init {
        fetchAirplane()
    }

    val airplaneLiveData = MutableLiveData<Airplane>()

    var airplane: Airplane? = null
        set(value) {
            field = value
            if (value != null) {
                name = value.name
                maxSpeed = if (value.maxSpeed != null) value.maxSpeed.toString() else null
                weight = if (value.weight != null) value.weight.toString() else null
            }

        }


    var name = ""
        set(value) {
            if (field != value) {
                field = value
            }
        }


    var nameError: Int? = null
        set(value) {
            if (field != value) {
                field = value
            }
        }



    var maxSpeed: String? = null
        set(value) {
            if (field != value) {
                field = value
            }
        }


    var weight: String? = null
        set(value) {
            if (field != value) {
                field = value
            }
        }

    private fun fetchAirplane() {
        makeRequest({
            airplanesService.getAirplaneById(airplaneId)
        }) { it ->
            airplaneLiveData.value = it
        }
    }

    fun saveAirplane() {
        makeRequest({
            //TODO image
            val request = AirplaneRequest(name, maxSpeed?.toInt(), weight?.toInt(), null)
            airplanesService.putAirplane(airplaneId, request)
        }) {
            navigateBack()
        }
    }

    private fun navigateToAirplaneDetails() {
        navigateBack()
        navDirectionLiveData.value = MainFragmentDirections.goToAirplaneDetails(airplaneId)
    }


}