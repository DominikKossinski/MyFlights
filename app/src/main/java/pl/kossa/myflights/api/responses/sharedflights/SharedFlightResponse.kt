package pl.kossa.myflights.api.responses.sharedflights

import pl.kossa.myflights.api.models.Flight
import pl.kossa.myflights.api.models.Image

data class SharedFlightResponse(
    val sharedFlightId: String,
    val flight: Flight,
    val ownerId: String,
    val sharedUserData: SharedUserData?
)

data class SharedUserData(
    val userId: String,
    val email: String?,
    val nick: String,
    val avatar: Image?,
)