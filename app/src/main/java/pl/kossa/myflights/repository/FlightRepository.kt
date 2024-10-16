package pl.kossa.myflights.repository

import pl.kossa.myflights.api.call.ApiResponse
import pl.kossa.myflights.api.requests.FlightRequest
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.api.services.FlightsService
import pl.kossa.myflights.architecture.BaseRepository
import pl.kossa.myflights.architecture.ResultWrapper
import pl.kossa.myflights.room.dao.FlightDao
import pl.kossa.myflights.room.entities.Flight
import pl.kossa.myflights.utils.PreferencesHelper

class FlightRepository(
    private val flightsService: FlightsService,
    private val flightDao: FlightDao,
    preferencesHelper: PreferencesHelper
) : BaseRepository(preferencesHelper) {

    suspend fun getFlights(): ResultWrapper<List<Flight>> {
        val response = makeRequest {
            flightsService.getAllFlights()
        }
        val flights = when (response) {
            is ApiResponse.Success -> response.value ?: emptyList()
            else -> emptyList()
        }
        flights.forEach {
            flightDao.insertFlight(Flight.fromApiFlight(it))
        }
        val value = flightDao.getAll()
        return when (response) {
            is ApiResponse.Success -> ResultWrapper.Success(value)
            is ApiResponse.GenericError -> ResultWrapper.GenericError(value, response.apiError)
            is ApiResponse.NetworkError -> ResultWrapper.NetworkError(
                value,
                response.networkErrorType
            )
        }
    }

    suspend fun getFlightById(flightId: String): ResultWrapper<Flight?> {
        val response = makeRequest {
            flightsService.getFLightById(flightId)
        }
        return when (response) {
            is ApiResponse.Success -> {
                response.value?.let {
                    flightDao.insertFlight(Flight.fromApiFlight(it))
                }
                ResultWrapper.Success(flightDao.getFlightById(flightId))
            }
            is ApiResponse.GenericError -> {
                when (response.apiError.code) {
                    HttpCode.NOT_FOUND.code -> {
                        val flight = flightDao.getFlightById(
                            flightId
                        )
                        preferencesHelper.userId?.let { userId ->
                            flight?.let { flightDao.delete(userId, it) }
                        }
                        ResultWrapper.GenericError(null, response.apiError)
                    }
                    else -> {
                        val flight = flightDao.getFlightById(flightId)
                        ResultWrapper.GenericError(flight, response.apiError)
                    }
                }
            }
            is ApiResponse.NetworkError -> {
                ResultWrapper.NetworkError(
                    flightDao.getFlightById(
                        flightId
                    ),
                    response.networkErrorType
                )
            }
        }
    }

    suspend fun createFlight(flightRequest: FlightRequest): ResultWrapper<String?> {
        val response = makeRequest {
            flightsService.postFlight(flightRequest)
        }
        return when (response) {
            is ApiResponse.Success -> {
                response.value?.entityId?.let {
                    getFlightById(it)
                }
                ResultWrapper.Success(response.value?.entityId)
            }
            is ApiResponse.GenericError -> ResultWrapper.GenericError(null, response.apiError)
            is ApiResponse.NetworkError -> ResultWrapper.NetworkError(
                null,
                response.networkErrorType
            )
        }
    }

    suspend fun saveFlight(flightId: String, flightRequest: FlightRequest): ResultWrapper<Unit?> {
        val response = makeRequest {
            flightsService.putFlight(flightId, flightRequest)
        }
        return when (response) {
            is ApiResponse.Success -> {
                getFlightById(flightId)
                ResultWrapper.Success(Unit)
            }
            is ApiResponse.GenericError -> {
                when (response.apiError.code) {
                    HttpCode.NOT_FOUND.code -> {
                        preferencesHelper.userId?.let {
                            flightDao.getFlightById(flightId)?.let { flight ->
                                flightDao.delete(it, flight)
                            }
                        }
                        ResultWrapper.GenericError(null, response.apiError)
                    }
                    else -> {
                        ResultWrapper.GenericError(null, response.apiError)
                    }
                }
            }
            is ApiResponse.NetworkError -> {
                ResultWrapper.NetworkError(null, response.networkErrorType)
            }
        }
    }

    suspend fun deleteFlight(flightId: String): ResultWrapper<Unit?> {
        val response = makeRequest {
            flightsService.deleteFlight(flightId)
        }
        val flight = flightDao.getFlightById(flightId)
        return when (response) {
            is ApiResponse.Success -> {
                flight?.let {
                    preferencesHelper.userId?.let { userId ->
                        flightDao.delete(userId, flight)
                    }
                }
                ResultWrapper.Success(Unit)
            }
            is ApiResponse.GenericError -> {
                when (response.apiError.code) {
                    HttpCode.NOT_FOUND.code -> {
                        preferencesHelper.userId?.let { userId ->
                            flight?.let { flightDao.delete(userId, flight) }
                        }
                        ResultWrapper.GenericError(null, response.apiError)
                    }
                    else -> {
                        ResultWrapper.GenericError(null, response.apiError)
                    }
                }
            }
            is ApiResponse.NetworkError -> {
                ResultWrapper.NetworkError(null, response.networkErrorType)
            }
        }
    }
}