package pl.kossa.myflights.repository

import pl.kossa.myflights.api.call.ApiResponse1
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
            is ApiResponse1.Success -> response.value ?: emptyList()
            else -> emptyList()
        }
        flights.forEach {
            flightDao.insertFlight(Flight.fromApiFlight(it))
        }
        val value = flightDao.getAll()
        return when (response) {
            is ApiResponse1.Success -> ResultWrapper.Success(value)
            is ApiResponse1.GenericError -> ResultWrapper.GenericError(value, response.apiError)
            is ApiResponse1.NetworkError -> ResultWrapper.NetworkError(
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
            is ApiResponse1.Success -> {
                response.value?.let {
                    flightDao.insertFlight(Flight.fromApiFlight(it))
                }
                ResultWrapper.Success(flightDao.getFlightById(flightId))
            }
            is ApiResponse1.GenericError -> {
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
            is ApiResponse1.NetworkError -> {
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
            is ApiResponse1.Success -> {
                response.value?.entityId?.let {
                    getFlightById(it)
                }
                ResultWrapper.Success(response.value?.entityId)
            }
            is ApiResponse1.GenericError -> ResultWrapper.GenericError(null, response.apiError)
            is ApiResponse1.NetworkError -> ResultWrapper.NetworkError(
                null,
                response.networkErrorType
            )
        }
    }

    suspend fun saveFlight(flightId: String, flightRequest: FlightRequest) {
        flightsService.putFlight(flightId, flightRequest)
        getFlightById(flightId)
    }

    suspend fun deleteFlight(flightId: String) {
        flightsService.deleteFlight(flightId)
        val flight = flightDao.getFlightById(flightId)
        flight?.let { f ->
            preferencesHelper.userId?.let { userId ->
                flightDao.delete(userId, f)
            }
        }
    }
}