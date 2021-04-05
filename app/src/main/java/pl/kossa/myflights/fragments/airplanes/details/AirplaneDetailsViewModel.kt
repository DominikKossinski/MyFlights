package pl.kossa.myflights.fragments.airplanes.details

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.api.services.AirplanesService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class AirplaneDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val airplanesService: AirplanesService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    init {
        fetchAirplane()
    }

    private val airplaneId = savedStateHandle.get<Int>("airplaneId")!!
    val airplaneLiveData = MutableLiveData<Airplane>()

    var airplane: Airplane? = null
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    val airplaneName
        get() = airplane?.name ?: ""

    fun fetchAirplane() {
        makeRequest({
            airplanesService.getAirplaneById(airplaneId)
        }) { it ->
            airplaneLiveData.value = it
        }
    }

    fun navigateToAirplaneEdit() {
        navDirectionLiveData.postValue(AirplaneDetailsFragmentDirections.goToAirplaneEdit(airplaneId))
    }

    fun deleteAirplane() {
        makeRequest({
            airplanesService.deleteAirplane(airplaneId)
        }
        ) {
            toastError.value = R.string.airplane_deleted
        }
    }
}
