package pl.kossa.myflights.api.responses

import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.api.models.Airport

data class StatisticsResponse(
    val flightHours: Double,
    val favouriteAirplane: Airplane?,
    val top5Airplanes: List<TopNElement<Airplane>>,
    val favouriteDepartureAirport: Airport?,
    val top5DepartureAirports: List<TopNElement<Airport>>,
    val favouriteArrivalAirport: Airport?,
    val top5ArrivalAirports: List<TopNElement<Airport>>,
)

data class TopNElement<Y>(val item: Y, val occurrences: Int)