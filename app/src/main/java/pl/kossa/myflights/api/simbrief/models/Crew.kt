package pl.kossa.myflights.api.simbrief.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "crew", strict = false)
data class Crew(
    @field:Element(name = "pilot_id")
    var pilotId: Long? = null,

    @field:Element(name = "cpt")
    var capitan: String? = null,
)
