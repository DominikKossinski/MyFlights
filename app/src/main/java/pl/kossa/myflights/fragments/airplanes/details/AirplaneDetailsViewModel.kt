package pl.kossa.myflights.fragments.airplanes.details

import androidx.databinding.Bindable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.R
import pl.kossa.myflights.api.ApiService
import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class AirplaneDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val apiService: ApiService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    init {
        fetchAirplane()
    }

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
//        makeRequest({ TODO
//            apiService.airplanesService.getAirplaneById(airplaneId)
//        }) { it ->
//            airplaneLiveData.value = it
//        }
    }

    fun navigateToAirplaneEdit() {
        //TODO    navController.navigate(AirplaneDetailsFragmentDirections.goToAirplaneEdit(airplaneId))
    }

    fun deleteAirplane() {
//        makeRequest({
//            apiService.airplanesService.deleteAirplane(airplaneId)
//        }
//        ) {
//            toastError.value = R.string.airplane_deleted
//            //TODO  navigateBack()
//        }
    }
}
