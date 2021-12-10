package pl.kossa.myflights.api.simbrief.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(strict = false)
data class Airport(
    @field:Element(name = "icao_code")
    var icaoCode: String? = null,

    @field:Element(name = "pos_lat")
    var posLat: Float? = null,

    @field:Element(name = "pos_long")
    var posLong: Float? = null,

    @field:Element(name = "elevation")
    var elevation: Float? = null,

    @field:Element(name = "name")
    var name: String? = null,
)
