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

    //TODO navlog

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

@Root(name = "alternate", strict = false)
data class Alternate(

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

    @field:Element(name = "route")
    var route: String? = null,
)

@Root(name = "Aircraft", strict = false)
data class Aircraft(
    @field:Element(name = "icaocode")
    var icaoCode: String? = null,

    @field:Element(name = "name")
    var name: String? = null,

    @field:Element(name = "max_passengers")
    var maxPassengers: Int? = null
)

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

@Root(name = "crew", strict = false)
data class Crew(
    @field:Element(name = "pilot_id")
    var pilotId: Long? = null,

    @field:Element(name = "cpt")
    var capitan: String? = null,
)

@Root(name = "images", strict = false)
data class Images(
    @field:Element(name = "directory")
    var directory: String? = null,

    @field:ElementList(name = "map", inline = true)
    var maps: List<Map>? = null
)

@Root(name = "map", strict = false)
data class Map(
    @field:Element(name = "name")
    var name: String? = null,

    @field:Element(name = "link")
    var link: String? = null
)
