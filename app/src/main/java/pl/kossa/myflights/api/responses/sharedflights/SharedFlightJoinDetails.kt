package pl.kossa.myflights.api.responses.sharedflights

import pl.kossa.myflights.api.models.Flight

data class SharedFlightJoinDetails(
    val flight: Flight,
    val ownerData: SharedUserData
)
