package pl.kossa.myflights.repository

import pl.kossa.myflights.api.call.ApiResponse1
import pl.kossa.myflights.api.models.SharedFlight
import pl.kossa.myflights.api.responses.sharedflights.SharedFlightJoinDetails
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

    suspend fun confirmSharedFlight(sharedFlightId: String): ResultWrapper<Unit?> {
        val response = makeRequest {
            sharedFlightsService.confirmSharedFlight(sharedFlightId)
        }
        return when (response) {
            is ApiResponse1.Success -> ResultWrapper.Success(Unit)
            is ApiResponse1.GenericError -> ResultWrapper.GenericError(null, response.apiError)
            is ApiResponse1.NetworkError -> ResultWrapper.NetworkError(
                null,
                response.networkErrorType
            )
        }
    }

    suspend fun joinSharedFlight(sharedFlightId: String): ResultWrapper<Unit?> {
        val response = makeRequest {
            sharedFlightsService.joinSharedFlight(sharedFlightId)
        }
        return when (response) {
            is ApiResponse1.Success -> ResultWrapper.Success(Unit)
            is ApiResponse1.GenericError -> ResultWrapper.GenericError(null, response.apiError)
            is ApiResponse1.NetworkError -> ResultWrapper.NetworkError(
                null,
                response.networkErrorType
            )
        }
    }

    suspend fun deleteSharedFlight(sharedFlightId: String): ResultWrapper<Unit?> {
        val response = makeRequest {
            sharedFlightsService.deleteSharedFlight(sharedFlightId)
        }
        return when (response) {
            is ApiResponse1.Success -> ResultWrapper.Success(Unit)
            is ApiResponse1.GenericError -> ResultWrapper.GenericError(null, response.apiError)
            is ApiResponse1.NetworkError -> ResultWrapper.NetworkError(
                null,
                response.networkErrorType
            )
        }
    }

    suspend fun resignFromSharedFlight(sharedFlightId: String): ResultWrapper<Unit?> {
        val response = makeRequest {
            sharedFlightsService.resignFromSharedFlight(sharedFlightId)
        }
        return when (response) {
            is ApiResponse1.Success -> ResultWrapper.Success(Unit)
            is ApiResponse1.GenericError -> ResultWrapper.GenericError(null, response.apiError)
            is ApiResponse1.NetworkError -> ResultWrapper.NetworkError(
                null,
                response.networkErrorType
            )
        }
    }

    suspend fun getSharedFlightJoinDetails(sharedFlightId: String): ResultWrapper<SharedFlightJoinDetails?> {
        val response = makeRequest {
            sharedFlightsService.getSharedFlightJoinDetails(sharedFlightId)
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


}

