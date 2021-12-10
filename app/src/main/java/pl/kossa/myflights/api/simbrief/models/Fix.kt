package pl.kossa.myflights.api.simbrief.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "fix", strict = false)
data class Fix(
    @field:Element(name = "ident")
    var ident: String? = null,

    @field:Element(name = "name")
    var name: String? = null,

    @field:Element(name = "type")
    var type: String? = null,

    @field:Element(name = "pos_lat")
    var posLat: Float? = null,

    @field:Element(name = "pos_long")
    var posLong: Float? = null,

    @field:Element(name = "via_airway")
    var viaAirway: String? = null

    //TODO fields
)
