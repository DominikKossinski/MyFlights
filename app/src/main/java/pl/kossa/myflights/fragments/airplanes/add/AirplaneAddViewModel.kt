package pl.kossa.myflights.fragments.airplanes.add

import androidx.databinding.Bindable
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.BR
import pl.kossa.myflights.R
import pl.kossa.myflights.api.requests.AirplaneRequest
import pl.kossa.myflights.api.services.AirplanesService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.fragments.airplanes.AirplanesFragmentDirections
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class AirplaneAddViewModel @Inject constructor(
    private val airplanesService: AirplanesService,
    preferencesHelper: PreferencesHelper
) :
    BaseViewModel(preferencesHelper) {


    @get:Bindable
    var name = ""
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.name)
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

    @get:Bindable
    var maxSpeed: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.maxSpeed)
            }
        }

    @get:Bindable
    var weight: String? = null
        set(value) {
            if (field != value) {
                field = value
                notifyPropertyChanged(BR.weight)
            }
        }


    fun postAirplane() {
        nameError = null
        if (name.isBlank()) {
            nameError = R.string.error_empty_name
            return
        }
        makeRequest({//TODO image
            val request = AirplaneRequest(name, maxSpeed?.toInt(), weight?.toInt(), null)
           airplanesService.postAirplane(request)
        }) { it ->
            navigateToDetails(it.entityId)
        }
    }

    private fun navigateToDetails(airplaneId: Int) {
        navigateBack()
        navDirectionLiveData.value = AirplanesFragmentDirections.goToAirplaneDetails(airplaneId)
    }
}
