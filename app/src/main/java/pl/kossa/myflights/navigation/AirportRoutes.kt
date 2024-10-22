package pl.kossa.myflights.navigation

import kotlinx.serialization.Serializable

@Serializable
object AirportsRoute

@Serializable
data class AirportRoute(
    val id: String,
    val icaoCode: String,
)


@Serializable
object  AddAirportRoute

