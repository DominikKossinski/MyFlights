package pl.kossa.myflights.api.models

data class SharedFlight(

    val sharedFlightId: String,

    val flightId: String,

    val ownerId: String,

    val sharedUserId: String?,

    val isConfirmed: Boolean

)