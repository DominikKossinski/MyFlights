package pl.kossa.myflights.repository

import pl.kossa.myflights.api.call.ApiResponse
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

    suspend fun getAirports(text: String=""): ResultWrapper<List<Airport>> {
        val response = makeRequest {
            airportsService.getAirports()
        }
        val airports = when (response) {
            is ApiResponse.Success -> response.value ?: emptyList()
            else -> emptyList()
        }
        airports.forEach {
            airportDao.insertAirport(Airport.fromApiAirport(it))
        }
        val value = preferencesHelper.userId?.let { airportDao.getAll(it, text) } ?: emptyList()
        return when (response) {
            is ApiResponse.Success -> ResultWrapper.Success(value)
            is ApiResponse.GenericError -> ResultWrapper.GenericError(value, response.apiError)
            is ApiResponse.NetworkError -> ResultWrapper.NetworkError(
                value,
                response.networkErrorType
            )
        }
    }

    suspend fun getAirportById(airportId: String): ResultWrapper<Airport?> {
        val response = makeRequest {
            airportsService.getAirportById(airportId)
        }
        if (response is ApiResponse.Success) {
            response.value?.let {
                airportDao.insertAirport(Airport.fromApiAirport(it))
            }
        }
        val airport =
            preferencesHelper.userId?.let { airportDao.getAirportById(it, airportId) }
        return when (response) {
            is ApiResponse.Success -> {
                ResultWrapper.Success(airport)
            }
            is ApiResponse.GenericError -> {
                when (response.apiError.code) {
                    HttpCode.NOT_FOUND.code -> {
                        airport?.let { airportDao.delete(it) }
                        ResultWrapper.GenericError(null, response.apiError)
                    }
                    else ->
                        ResultWrapper.GenericError(airport, response.apiError)
                }
            }
            is ApiResponse.NetworkError -> ResultWrapper.NetworkError(
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
            is ApiResponse.Success -> {
                response.value?.entityId?.let {
                    getAirportById(it)
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

    suspend fun saveAirport(
        airportId: String,
        airportRequest: AirportRequest
    ): ResultWrapper<Unit?> {
        val response = makeRequest {
            airportsService.putAirport(airportId, airportRequest)
        }
        return when (response) {
            is ApiResponse.Success -> {
                getAirportById(airportId)
                ResultWrapper.Success(Unit)
            }
            is ApiResponse.GenericError -> {
                when (response.apiError.code) {
                    HttpCode.NOT_FOUND.code -> {
                        val airport = preferencesHelper.userId?.let {
                            airportDao.getAirportById(
                                it, airportId
                            )
                        }
                        airport?.let { airportDao.delete(it) }
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

    suspend fun deleteAirport(airportId: String): ResultWrapper<Unit?> {
        val response = makeRequest {
            airportsService.deleteAirport(airportId)
        }
        val airport = preferencesHelper.userId?.let {
            airportDao.getAirportById(it, airportId)
        }
        return when (response) {
            is ApiResponse.Success -> {
                airport?.let {
                    airportDao.delete(it)
                }
                ResultWrapper.Success(Unit)
            }
            is ApiResponse.GenericError -> {
                when (response.apiError.code) {
                    HttpCode.NOT_FOUND.code -> {
                        airport?.let { airportDao.delete(it) }
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