package pl.kossa.myflights.repository

import pl.kossa.myflights.api.call.ApiResponse
import pl.kossa.myflights.api.requests.RunwayRequest
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.api.services.RunwaysService
import pl.kossa.myflights.architecture.BaseRepository
import pl.kossa.myflights.architecture.ResultWrapper
import pl.kossa.myflights.room.dao.RunwayDao
import pl.kossa.myflights.room.entities.Runway
import pl.kossa.myflights.utils.PreferencesHelper

class RunwayRepository(
    private val runwaysService: RunwaysService,
    private val runwayDao: RunwayDao,
    preferencesHelper: PreferencesHelper
) : BaseRepository(preferencesHelper) {

    suspend fun getRunwayById(airportId: String, runwayId: String): ResultWrapper<Runway?> {
        val response = makeRequest {
            runwaysService.getRunwayById(airportId, runwayId)
        }
        return when (response) {
            is ApiResponse.Success -> {
                response.value?.let {
                    runwayDao.insertRunway(Runway.fromApiRunway(airportId, it))
                }
                ResultWrapper.Success(preferencesHelper.userId?.let {
                    runwayDao.getRunwayById(
                        it, airportId, runwayId
                    )
                })
            }
            is ApiResponse.GenericError -> {
                when (response.apiError.code) {
                    HttpCode.NOT_FOUND.code -> {
                        val runway = preferencesHelper.userId?.let {
                            runwayDao.getRunwayById(
                                it, airportId, runwayId
                            )
                        }
                        runway?.let { runwayDao.delete(runway) }
                        ResultWrapper.GenericError(null, response.apiError)
                    }
                    else -> {
                        val runway = preferencesHelper.userId?.let {
                            runwayDao.getRunwayById(
                                it, airportId, runwayId
                            )
                        }
                        ResultWrapper.GenericError(runway, response.apiError)
                    }
                }
            }
            is ApiResponse.NetworkError -> {
                ResultWrapper.NetworkError(preferencesHelper.userId?.let {
                    runwayDao.getRunwayById(
                        it, airportId, runwayId
                    )
                }, response.networkErrorType)
            }
        }
    }

    suspend fun createRunway(
        airportId: String,
        runwayRequest: RunwayRequest
    ): ResultWrapper<String?> {
        val response = makeRequest {
            runwaysService.postRunway(airportId, runwayRequest)
        }
        return when (response) {
            is ApiResponse.Success -> {
                response.value?.entityId?.let {
                    getRunwayById(airportId, it)
                }
                ResultWrapper.Success(response.value?.entityId)
            }
            is ApiResponse.GenericError -> {
                ResultWrapper.GenericError(null, response.apiError)
            }
            is ApiResponse.NetworkError -> {
                ResultWrapper.NetworkError(null, response.networkErrorType)
            }
        }
    }

    suspend fun savaRunway(
        airportId: String,
        runwayId: String,
        runwayRequest: RunwayRequest
    ): ResultWrapper<Unit?> {
        val response = makeRequest {
            runwaysService.putRunway(airportId, runwayId, runwayRequest)
        }
        return when (response) {
            is ApiResponse.Success -> {
                getRunwayById(airportId, runwayId)
                ResultWrapper.Success(Unit)
            }
            is ApiResponse.GenericError -> {
                when (response.apiError.code) {
                    HttpCode.NOT_FOUND.code -> {
                        val runway = preferencesHelper.userId?.let {
                            runwayDao.getRunwayById(
                                it, airportId, runwayId
                            )
                        }
                        runway?.let { runwayDao.delete(it) }
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

    suspend fun deleteRunway(airportId: String, runwayId: String): ResultWrapper<Unit?> {
        val response = makeRequest {
            runwaysService.deleteRunway(airportId, runwayId)
        }
        val runway = preferencesHelper.userId?.let {
            runwayDao.getRunwayById(it, airportId, runwayId)
        }
        return when (response) {
            is ApiResponse.Success -> {
                runway?.let { entity ->
                    runwayDao.delete(entity)
                }
                ResultWrapper.Success(Unit)
            }
            is ApiResponse.GenericError -> {
                when (response.apiError.code) {
                    HttpCode.NOT_FOUND.code -> {
                        runway?.let { runwayDao.delete(it) }
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