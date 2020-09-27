package pl.kossa.myflights.fragments.airports.adapter

import androidx.databinding.Bindable
import pl.kossa.myflights.api.models.Airport
import pl.kossa.myflights.architecture.BaseRecyclerViewModel

abstract class AirportViewModel(
    airport: Airport
) : BaseRecyclerViewModel<Airport>(airport) {

    @get:Bindable
    val name = "${airport.name} (${airport.shortcut})"


    @get:Bindable
    val city = airport.city
}