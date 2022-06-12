package pl.kossa.myflights.repository

import pl.kossa.myflights.api.services.SharedFlightsService

class SharedFlightRepository(
    private val sharedFlightsService: SharedFlightsService
) {

    suspend fun getPendingSharedFlights() = sharedFlightsService.getPendingSharedFlights()

    suspend fun getSharedFlight(sharedFlightId: String) =
        sharedFlightsService.getSharedFlight(sharedFlightId)

    suspend fun shareFlight(flightId: String) = sharedFlightsService.shareFlight(flightId)

    suspend fun confirmSharedFlight(sharedFlightId: String) =
        sharedFlightsService.confirmSharedFlight(sharedFlightId)

    suspend fun joinSharedFlight(sharedFlightId: String) =
        sharedFlightsService.joinSharedFlight(sharedFlightId)

    suspend fun deleteSharedFlight(sharedFlightId: String) =
        sharedFlightsService.deleteSharedFlight(sharedFlightId)

    suspend fun resignFromSharedFlight(sharedFlightId: String) =
        sharedFlightsService.resignFromSharedFlight(sharedFlightId)

    suspend fun getSharedFlightJoinDetails(sharedFlightId: String) =
        sharedFlightsService.getSharedFlightJoinDetails(sharedFlightId)


}