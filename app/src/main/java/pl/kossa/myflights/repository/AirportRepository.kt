package pl.kossa.myflights.repository

import pl.kossa.myflights.api.call.ApiResponse1
import pl.kossa.myflights.api.requests.AirportRequest
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.api.services.AirportsService
import pl.kossa.myflights.architecture.BaseRepository
import pl.kossa.myflights.architecture.ResultWrapper
import pl.kossa.myflights.room.dao.AirportDao
import pl.kossa.myflights.room.entities.Airport
import pl.kossa.myflights.utils.PreferencesHelper

class AirportRepository(
    private val airportsService: AirportsService,
    private val airportDao: AirportDao,
    preferencesHelper: PreferencesHelper
) : BaseRepository(preferencesHelper) {

    suspend fun getAirports(): ResultWrapper<List<Airport>> {
        val response = makeRequest {
            airportsService.getAirports()
        }
        val airports = when (response) {
            is ApiResponse1.Success -> response.value ?: emptyList()
            else -> emptyList()
        }
        airports.forEach {
            airportDao.insertAirport(Airport.fromApiAirport(it))
        }
        val value = preferencesHelper.userId?.let { airportDao.getAll(it) } ?: emptyList()
        return when (response) {
            is ApiResponse1.Success -> ResultWrapper.Success(value)
            is ApiResponse1.GenericError -> ResultWrapper.GenericError(value, response.apiError)
            is ApiResponse1.NetworkError -> ResultWrapper.NetworkError(
                value,
                response.networkErrorType
            )
        }
    }

    suspend fun getAirportById(airportId: String): ResultWrapper<Airport?> {
        val response = makeRequest {
            airportsService.getAirportById(airportId)
        }
        if (response is ApiResponse1.Success) {
            response.value?.let {
                airportDao.insertAirport(Airport.fromApiAirport(it))
            }
        }
        val airport =
            preferencesHelper.userId?.let { airportDao.getAirportById(it, airportId) }
        return when (response) {
            is ApiResponse1.Success -> {
                ResultWrapper.Success(airport)
            }
            is ApiResponse1.GenericError -> {
                when (response.apiError.code) {
                    HttpCode.NOT_FOUND.code -> {
                        airport?.let { airportDao.delete(it) }
                        ResultWrapper.GenericError(null, response.apiError)
                    }
                    else ->
                        ResultWrapper.GenericError(airport, response.apiError)
                }
            }
            is ApiResponse1.NetworkError -> ResultWrapper.NetworkError(
                airport,
                response.networkErrorType
            )
        }
    }

    suspend fun createAirport(airportRequest: AirportRequest): ResultWrapper<String?> {
        val response = makeRequest {
            airportsService.postAirport(airportRequest)
        }
        return when (response) {
            is ApiResponse1.Success -> {
                response.value?.entityId?.let {
                    getAirportById(it)
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

    suspend fun saveAirport(
        airportId: String,
        airportRequest: AirportRequest
    ): ResultWrapper<Unit?> {
        val response = makeRequest {
            airportsService.putAirport(airportId, airportRequest)
        }
        return when (response) {
            is ApiResponse1.Success -> {
                getAirportById(airportId)
                ResultWrapper.Success(Unit)
            }
            is ApiResponse1.GenericError -> {
                ResultWrapper.GenericError(null, response.apiError)
            }
            is ApiResponse1.NetworkError -> {
                ResultWrapper.NetworkError(null, response.networkErrorType)
            }
        }
    }

    suspend fun deleteAirport(airportId: String): ResultWrapper<Unit?> {
        val response = makeRequest {
            airportsService.deleteAirport(airportId)
        }
        return when (response) {
            is ApiResponse1.Success -> {
                preferencesHelper.userId?.let {
                    val airplane = airportDao.getAirportById(it, airportId)
                    airplane?.let { entity ->
                        airportDao.delete(entity)
                    }
                }
                ResultWrapper.Success(Unit)
            }
            is ApiResponse1.GenericError -> {
                ResultWrapper.GenericError(null, response.apiError)
            }
            is ApiResponse1.NetworkError -> {
                ResultWrapper.NetworkError(null, response.networkErrorType)
            }
        }
    }
}