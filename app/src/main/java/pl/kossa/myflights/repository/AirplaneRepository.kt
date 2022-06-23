package pl.kossa.myflights.repository

import pl.kossa.myflights.api.call.ApiResponse1
import pl.kossa.myflights.api.requests.AirplaneRequest
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.api.services.AirplanesService
import pl.kossa.myflights.architecture.BaseRepository
import pl.kossa.myflights.architecture.ResultWrapper
import pl.kossa.myflights.room.dao.AirplaneDao
import pl.kossa.myflights.room.entities.Airplane
import pl.kossa.myflights.utils.PreferencesHelper


class AirplaneRepository(
    private val airplanesService: AirplanesService,
    private val airplaneDao: AirplaneDao,
    preferencesHelper: PreferencesHelper
) : BaseRepository(preferencesHelper) {

    suspend fun getAirplanes(): ResultWrapper<List<Airplane>> {
        val response = makeRequest(airplanesService::getAirplanes)
        val airplanes = when (response) {
            is ApiResponse1.Success -> response.value ?: emptyList()
            else -> emptyList()
        }
        airplanes.forEach {
            airplaneDao.insertAirplane(Airplane.fromApiAirplane(it))
        }
        val value = preferencesHelper.userId?.let { airplaneDao.getAll(it) } ?: emptyList()
        return when (response) {
            is ApiResponse1.Success -> ResultWrapper.Success(value)
            is ApiResponse1.GenericError -> ResultWrapper.GenericError(value, response.apiError)
            is ApiResponse1.NetworkError -> ResultWrapper.NetworkError(
                value,
                response.networkErrorType
            )
        }
    }

    suspend fun getAirplaneById(airplaneId: String): ResultWrapper<Airplane?> {
        val response = makeRequest {
            airplanesService.getAirplaneById(airplaneId)
        }
        return when (response) {
            is ApiResponse1.Success -> {
                response.value?.let {
                    airplaneDao.insertAirplane(Airplane.fromApiAirplane(it))
                }
                ResultWrapper.Success(preferencesHelper.userId?.let {
                    airplaneDao.getAirplaneById(
                        it,
                        airplaneId
                    )
                })
            }
            is ApiResponse1.GenericError -> {
                when (response.apiError.code) {
                    HttpCode.NOT_FOUND.code -> {
                        val airplane = preferencesHelper.userId?.let {
                            airplaneDao.getAirplaneById(
                                it,
                                airplaneId
                            )
                        }
                        airplane?.let { airplaneDao.delete(airplane) }
                        ResultWrapper.GenericError(null, response.apiError)
                    }
                    else -> {
                        val airplane = preferencesHelper.userId?.let {
                            airplaneDao.getAirplaneById(
                                it,
                                airplaneId
                            )
                        }
                        ResultWrapper.GenericError(airplane, response.apiError)
                    }
                }
            }
            is ApiResponse1.NetworkError -> {
                ResultWrapper.NetworkError(preferencesHelper.userId?.let {
                    airplaneDao.getAirplaneById(
                        it,
                        airplaneId
                    )
                }, response.networkErrorType)
            }
        }
    }

    suspend fun createAirplane(airplaneRequest: AirplaneRequest): ResultWrapper<String?> {
        val response = makeRequest {
            airplanesService.postAirplane(airplaneRequest)
        }
        return when (response) {
            is ApiResponse1.Success -> {
                response.value?.entityId?.let {
                    getAirplaneById(it)
                }
                ResultWrapper.Success(response.value?.entityId)
            }
            is ApiResponse1.GenericError -> {
                ResultWrapper.GenericError(null, response.apiError)
            }
            is ApiResponse1.NetworkError -> {
                ResultWrapper.NetworkError(null, response.networkErrorType)
            }
        }
    }

    suspend fun saveAirplane(
        airplaneId: String,
        airplaneRequest: AirplaneRequest
    ): ResultWrapper<Unit?> {
        val response = makeRequest {
            airplanesService.putAirplane(airplaneId, airplaneRequest)
        }
        return when (response) {
            is ApiResponse1.Success -> {
                getAirplaneById(airplaneId)
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

    suspend fun deleteAirplane(airplaneId: String): ResultWrapper<Unit?> {
        val response = makeRequest {
            airplanesService.deleteAirplane(airplaneId)
        }
        return when (response) {
            is ApiResponse1.Success -> {
                val airplane = airplaneDao.getAirplaneById(airplaneId, airplaneId)
                airplane?.let { entity ->
                    airplaneDao.delete(entity)
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