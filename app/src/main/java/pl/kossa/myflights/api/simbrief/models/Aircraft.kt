package pl.kossa.myflights.api.simbrief.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "Aircraft", strict = false)
data class Aircraft(
    @field:Element(name = "icaocode")
    var icaoCode: String? = null,

    @field:Element(name = "name")
    var name: String? = null,

    @field:Element(name = "max_passengers")
    var maxPassengers: Int? = null
)
