package pl.kossa.myflights.api.responses.flights

import pl.kossa.myflights.api.models.Flight
import pl.kossa.myflights.api.responses.sharedflights.SharedUserData

data class FlightResponse(
    val flight: Flight,
    val ownerData: SharedUserData,
    val sharedUsers: List<ShareData>
)

data class ShareData(
    val sharedFlightId: String,
    val userData: SharedUserData?,
    val isConfirmed: Boolean
)