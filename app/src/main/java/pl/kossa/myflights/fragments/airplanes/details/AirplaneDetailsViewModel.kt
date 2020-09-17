package pl.kossa.myflights.fragments.airplanes.details

import androidx.navigation.NavController
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper

class AirplaneDetailsViewModel(
    private val airplaneId: Int,
    navController: NavController,
    preferencesHelper: PreferencesHelper
) :
    BaseViewModel(navController, preferencesHelper) {

}
