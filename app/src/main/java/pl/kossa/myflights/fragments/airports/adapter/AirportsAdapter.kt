package pl.kossa.myflights.fragments.airports.adapter

import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Airport
import pl.kossa.myflights.architecture.BaseBindingRvAdapter

class AirportsAdapter : BaseBindingRvAdapter<Airport>() {

    override val layoutId = R.layout.element_airport
}