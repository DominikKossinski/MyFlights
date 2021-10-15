package pl.kossa.myflights.fragments.airplanes

import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.ResponseBody
import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.api.responses.ApiErrorBody
import pl.kossa.myflights.api.services.AirplanesService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.fragments.main.MainFragmentDirections
import pl.kossa.myflights.utils.PreferencesHelper
import retrofit2.Converter
import javax.inject.Inject

@HiltViewModel
class AirplanesViewModel @Inject constructor(
    private val airplanesService: AirplanesService,
    errorBodyConverter: Converter<ResponseBody, ApiErrorBody>,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(errorBodyConverter, preferencesHelper) {

    val airplanesList = MutableLiveData<List<Airplane>>()

    fun fetchAirplanes() {
        makeRequest(airplanesService::getAirplanes) {
            airplanesList.value = it
        }
    }

    fun deleteAirplane(airplaneId: String) {
        makeRequest({ airplanesService.deleteAirplane(airplaneId) }) {
            setToastError(R.string.airplane_deleted)
            fetchAirplanes()
        }
    }

    fun navigateToAddAirplane() {
        navDirectionLiveData.value = MainFragmentDirections.goToAirplaneAdd()
    }

    fun navigateToAirplaneDetails(airplaneId: String) {
        navDirectionLiveData.value = MainFragmentDirections.goToAirplaneDetails(airplaneId)
    }
}
