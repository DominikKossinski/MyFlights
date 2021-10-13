package pl.kossa.myflights.api.models

import java.util.*

data class Flight(
    val flightId: String,

    val note: String?,

    val distance: Int?,

    val imageUrl: String?,

    val departureDate: Date,

    val arrivalDate: Date,

    val userId: String,

    val airplane: Airplane,

    val departureAirport: Airport,

    val departureRunway: Runway,

    val arrivalAirport: Airport,

    val arrivalRunway: Runway
)