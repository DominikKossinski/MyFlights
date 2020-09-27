package pl.kossa.myflights.fragments.airplanes.adapter

import pl.kossa.myflights.R
import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.architecture.BaseBindingRvAdapter

class AirplanesAdapter : BaseBindingRvAdapter<Airplane>() {

    override val layoutId = R.layout.element_airplane
}