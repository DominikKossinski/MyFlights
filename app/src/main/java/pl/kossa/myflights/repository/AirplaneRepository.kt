package pl.kossa.myflights.repository

import pl.kossa.myflights.api.services.AirplanesService
import pl.kossa.myflights.room.dao.AirplaneDao
import pl.kossa.myflights.room.entities.Airplane

class AirplaneRepository(
    private val airplanesService: AirplanesService,
    private val airplaneDao: AirplaneDao
) {

    suspend fun getAirplanes(userId: String): List<Airplane> {
        val response = airplanesService.getAirplanes()
        val airplanes = response.body ?: emptyList()
        airplanes.forEach {
            airplaneDao.insertAirplane(Airplane.fromApiAirplane(it))
        }
        return airplaneDao.getAll(userId)
    }

    suspend fun getAirplaneById(userId: String, airplaneId: String): Airplane? {
        val response = airplanesService.getAirplaneById(airplaneId)
        response.body?.let {
            airplaneDao.insertAirplane(Airplane.fromApiAirplane(it))
        }
        return airplaneDao.getAirplaneById(userId, airplaneId)
    }
}