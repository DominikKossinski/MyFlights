package pl.kossa.myflights.api.simbrief.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "OFP", strict = false)
data class OFP @JvmOverloads constructor(
    @field:Element(name = "params")
    var params: FlightPlanParams? = null,
    @field:Element(name = "general")
    var general: GeneralParams? = null,
    @field:Element(name = "origin")
    var origin: Airport? = null,
    @field:Element(name = "destination")
    var destination: Airport? = null,
    @field:Element(name = "alternate")
    var alternate: Alternate? = null,

    @field:ElementList(name = "navlog")
    var navlog: List<Fix>? = null,

    @field:Element(name = "aircraft")
    var aircraft: Aircraft? = null,

    @field:Element(name = "fuel")
    var fuel: Fuel? = null,
    //TODO times
    //TODO weights
    //TODO impacts
    @field:Element(name = "crew")
    var crew: Crew? = null,
    //TODO notams
    //TODO weather

    //TODO files
    @field:Element(name = "images")
    var images: Images? = null
    //TODO api params
)

@Root(name = "params", strict = false)
data class FlightPlanParams @JvmOverloads constructor(
    @field:Element(name = "request_id")
    var requestId: Long? = null,
    @field:Element(name = "user_id")
    var userId: Long? = null,
    //TODO
)


@Root(name = "general", strict = false)
data class GeneralParams @JvmOverloads constructor(
    @field:Element(name = "icao_airline")
    var icaoAirLine: String? = null,
    @field:Element(name = "route")
    var route: String? = null
    //TODO
)
