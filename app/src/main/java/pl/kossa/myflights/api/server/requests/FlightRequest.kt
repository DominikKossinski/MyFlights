package pl.kossa.myflights.api.server.requests

import java.util.*

data class FlightRequest(
    val note: String?,

    val distance: Int?,

    val imageId: String?,

    val departureDate: Date,

    val arrivalDate: Date,

    val airplaneId: String,

    val departureAirportId: String,

    val departureRunwayId: String,

    val arrivalAirportId: String,

    val arrivalRunwayId: String
)

