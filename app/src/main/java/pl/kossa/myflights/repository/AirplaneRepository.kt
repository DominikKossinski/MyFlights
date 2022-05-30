package pl.kossa.myflights.repository

import pl.kossa.myflights.api.requests.AirplaneRequest
import pl.kossa.myflights.api.services.AirplanesService
import pl.kossa.myflights.room.dao.AirplaneDao
import pl.kossa.myflights.room.entities.Airplane
import pl.kossa.myflights.utils.PreferencesHelper

class AirplaneRepository(
    private val airplanesService: AirplanesService,
    private val airplaneDao: AirplaneDao,
    private val preferencesHelper: PreferencesHelper
) {

    suspend fun getAirplanes(): List<Airplane> {
        val response = airplanesService.getAirplanes()
        val airplanes = response.body ?: emptyList()
        airplanes.forEach {
            airplaneDao.insertAirplane(Airplane.fromApiAirplane(it))
        }
        return preferencesHelper.userId?.let { airplaneDao.getAll(it) } ?: emptyList()
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