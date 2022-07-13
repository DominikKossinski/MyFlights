package pl.kossa.myflights.repository

import pl.kossa.myflights.api.call.ApiResponse1
import pl.kossa.myflights.api.models.SharedFlight
import pl.kossa.myflights.api.responses.sharedflights.SharedFlightResponse
import pl.kossa.myflights.api.services.SharedFlightsService
import pl.kossa.myflights.architecture.BaseRepository
import pl.kossa.myflights.architecture.ResultWrapper
import pl.kossa.myflights.utils.PreferencesHelper

class SharedFlightRepository(
    private val sharedFlightsService: SharedFlightsService,
    preferencesHelper: PreferencesHelper
) : BaseRepository(preferencesHelper) {

    suspend fun getPendingSharedFlights(): ResultWrapper<List<SharedFlightResponse>> {
        val response = sharedFlightsService.getPendingSharedFlights()
        // todo save data locally
        return when (response) {
            is ApiResponse1.Success -> {
                ResultWrapper.Success(response.value ?: emptyList())
            }
            is ApiResponse1.GenericError -> ResultWrapper.GenericError(
                emptyList(),
                response.apiError
            )
            is ApiResponse1.NetworkError -> ResultWrapper.NetworkError(
                emptyList(),
                response.networkErrorType
            )
        }
    }

    suspend fun getSharedFlight(sharedFlightId: String): ResultWrapper<SharedFlightResponse?> {
        val response = makeRequest {
            sharedFlightsService.getSharedFlight(sharedFlightId)
        }
        return when (response) {
            is ApiResponse1.Success -> ResultWrapper.Success(response.value)
            is ApiResponse1.GenericError -> ResultWrapper.GenericError(null, response.apiError)
            is ApiResponse1.NetworkError -> ResultWrapper.NetworkError(
                null,
                response.networkErrorType
            )
        }
    }

    suspend fun shareFlight(flightId: String): ResultWrapper<SharedFlight?> {
        val response = makeRequest {
            sharedFlightsService.shareFlight(flightId)
        }
        return when (response) {
            is ApiResponse1.Success -> ResultWrapper.Success(response.value)
            is ApiResponse1.GenericError -> ResultWrapper.GenericError(null, response.apiError)
            is ApiResponse1.NetworkError -> ResultWrapper.NetworkError(
                null,
                response.networkErrorType
            )
        }
    }

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