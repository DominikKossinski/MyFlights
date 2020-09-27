package pl.kossa.myflights.fragments.airplanes.adapter

import androidx.databinding.Bindable
import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.architecture.BaseRecyclerViewModel

abstract class AirplaneViewModel(
    airplane: Airplane
) : BaseRecyclerViewModel<Airplane>(airplane) {

    @get:Bindable
    val name = airplane.name

    @get:Bindable
    val speedVisibility = airplane.maxSpeed != null

    @get:Bindable
    val speed = airplane.maxSpeed?.toString()
}