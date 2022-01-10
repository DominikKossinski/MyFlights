package pl.kossa.myflights.api.responses

import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.api.models.Airport

data class StatisticsResponse(
    val flightHours: Double,
    val favouriteAirplane: Airplane?,
    val favouriteDepartureAirport: Airport?,
    val favouriteArrivalAirport: Airport?,
)