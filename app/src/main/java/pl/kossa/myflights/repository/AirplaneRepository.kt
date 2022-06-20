package pl.kossa.myflights.repository

import pl.kossa.myflights.api.call.ApiResponse1
import pl.kossa.myflights.api.exceptions.ApiServerException
import pl.kossa.myflights.api.exceptions.NoInternetException
import pl.kossa.myflights.api.exceptions.UnauthorizedException
import pl.kossa.myflights.api.requests.AirplaneRequest
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.ApiErrorBody
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
): BaseRepository(preferencesHelper) {

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
            is ApiResponse1.NetworkError -> ResultWrapper.NetworkError(value)
        }
    }

    suspend fun getAirplaneById(airplaneId: String): Airplane? {
        val response = airplanesService.getAirplaneById(airplaneId)
        response.body?.let {
            airplaneDao.insertAirplane(Airplane.fromApiAirplane(it))
        }
        return preferencesHelper.userId?.let { airplaneDao.getAirplaneById(it, airplaneId) }
    }

    suspend fun createAirplane(airplaneRequest: AirplaneRequest): String? {
        val response = airplanesService.postAirplane(airplaneRequest)
        response.body?.entityId?.let {
            getAirplaneById(it)
        }
        return response.body?.entityId
    }

    suspend fun saveAirplane(airplaneId: String, airplaneRequest: AirplaneRequest) {
        airplanesService.putAirplane(airplaneId, airplaneRequest)
        getAirplaneById(airplaneId)
    }

    suspend fun deleteAirplane(airplaneId: String) {
        airplanesService.deleteAirplane(airplaneId)
        preferencesHelper.userId?.let {
            val airplane = airplaneDao.getAirplaneById(it, airplaneId)
            airplane?.let { entity ->
                airplaneDao.delete(entity)
            }
        }
    }
}