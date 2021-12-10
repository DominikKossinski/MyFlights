package pl.kossa.myflights.fragments.main

import dagger.hilt.android.lifecycle.HiltViewModel
import pl.kossa.myflights.ListsNavGraphDirections
import pl.kossa.myflights.architecture.BaseViewModel
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    preferencesHelper: PreferencesHelper
) : BaseViewModel(preferencesHelper) {

    fun goToFlights() = ListsNavGraphDirections.goToFlights()

    fun goToAirplanes() = ListsNavGraphDirections.goToAirplanes()

    fun goToAirports() = ListsNavGraphDirections.goToAirports()

    fun goToPlannedFlights() = ListsNavGraphDirections.goToPlannedFlights()

    fun navigateToProfile() {
        navigate(MainFragmentDirections.goToProfile())
    }
}