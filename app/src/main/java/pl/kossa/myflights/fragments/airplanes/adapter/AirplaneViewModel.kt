package pl.kossa.myflights.fragments.airplanes.adapter

import androidx.databinding.Bindable
import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.architecture.BaseObservable

class AirplaneViewModel(
    airplane: Airplane
) : BaseObservable() {

    @get:Bindable
    val name = airplane.name

    @get:Bindable
    val speedVisibility = airplane.maxSpeed != null

    @get:Bindable
    val speed = airplane.maxSpeed?.toString()
}