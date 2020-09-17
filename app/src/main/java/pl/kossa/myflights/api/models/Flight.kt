package pl.kossa.myflights.api.models

import java.util.*

data class Flight(
    val flightId: Int,

    val note: String?,

    val distance: Int?,

    val imageUrl: String?,

    val startDate: Date,

    val endDate: Date,

    val userId: String,

    val airplane: Airplane,

    val departureRunway: Runway,


    val arrivalRunway: Runway
)