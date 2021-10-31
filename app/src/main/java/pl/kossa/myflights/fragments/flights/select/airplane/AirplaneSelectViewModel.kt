package pl.kossa.myflights.fragments.flights.select.airplane

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.ResponseBody
import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.api.responses.ApiErrorBody
import pl.kossa.myflights.api.services.AirplanesService
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import retrofit2.Converter
import javax.inject.Inject

@HiltViewModel
class AirplaneSelectViewModel @Inject constructor(
    private val airplanesService: AirplanesService,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    val airplanesList = MutableStateFlow<List<Airplane>>(emptyList())

    init {
        fetchAirplanes("")
    }

    fun fetchAirplanes(text: String) {
        makeRequest {
            val response = airplanesService.getAirplanes(text)
            response.body?.let {
                airplanesList.value = it
            }
        }
    }

}