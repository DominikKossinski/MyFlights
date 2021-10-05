package pl.kossa.myflights.activities.main

import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.MainNavGraphDirections
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {


    fun goToAirplanes() {
//        navDirectionLiveData.value = MainNavGraphDirections.goToAirplanes()
    }

    fun goToFlights() {
//        navDirectionLiveData.value = MainNavGraphDirections.goToFlights()
    }

    fun goToAirports() {
//        navDirectionLiveData.value = MainNavGraphDirections.goToAirports()
    }
}
