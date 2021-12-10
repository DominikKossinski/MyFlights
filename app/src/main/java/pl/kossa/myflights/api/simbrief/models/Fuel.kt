package pl.kossa.myflights.api.simbrief.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "fuel", strict = false)
data class Fuel(
    @field:Element(name = "taxi")
    var taxi: Int? = null,

    @field:Element(name = "enroute_burn")
    var enrouteBurn: Int? = null,

    @field:Element(name = "contingency")
    var contingency: Int? = null,

    @field:Element(name = "alternate_burn")
    var alternateBurn: Int? = null,

    @field:Element(name = "reserve")
    var reserve: String? = null,

    @field:Element(name = "min_takeoff")
    var minTakeoff: Int? = null,

    @field:Element(name = "plan_ramp")
    var planRamp: Int? = null,

    @field:Element(name = "plan_landing")
    var planLanding: Int? = null,

    @field:Element(name = "avg_fuel_flow")
    var avgFuelFlow: Int? = null,

    @field:Element(name = "max_tanks")
    var maxTanks: Int? = null
)
