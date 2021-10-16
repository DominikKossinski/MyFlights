package pl.kossa.myflights.fragments.main

import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.ResponseBody
import pl.kossa.myflights.ListsNavGraphDirections
import pl.kossa.myflights.api.responses.ApiErrorBody
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import retrofit2.Converter
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    errorBodyConverter: Converter<ResponseBody, ApiErrorBody>,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(errorBodyConverter, preferencesHelper) {

    fun goToFlights() = ListsNavGraphDirections.goToFlights()

    fun goToAirplanes() = ListsNavGraphDirections.goToAirplanes()

    fun goToAirports() = ListsNavGraphDirections.goToAirports()

    fun goToProfile() = ListsNavGraphDirections.goToProfile()
}