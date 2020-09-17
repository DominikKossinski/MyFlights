package pl.kossa.myflights.activities.main

import androidx.navigation.NavController
import pl.kossa.myflights.MainNavGraphDirections
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.fragments.airplanes.AirplanesFragmentDirections
import pl.kossa.myflights.utils.PreferencesHelper

class MainActivityViewModel(
    navController: NavController,
    preferencesHelper: PreferencesHelper
) : BaseViewModel(navController, preferencesHelper) {


    fun goToAirplanes() {
        navController.navigate(MainNavGraphDirections.goToAirplanes())
    }

    fun goToFlights() {
        navController.navigate(MainNavGraphDirections.goToFlights())
    }

    fun goToAirplaneAdd() {
        navController.navigate(AirplanesFragmentDirections.goToAirplaneAdd())
    }
}
