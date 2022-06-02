package pl.kossa.myflights.repository

import pl.kossa.myflights.api.requests.RunwayRequest
import pl.kossa.myflights.api.services.RunwaysService
import pl.kossa.myflights.room.dao.RunwayDao
import pl.kossa.myflights.room.entities.Runway
import pl.kossa.myflights.utils.PreferencesHelper

class RunwayRepository(
    private val runwaysService: RunwaysService,
    private val runwayDao: RunwayDao,
    private val preferencesHelper: PreferencesHelper
) {

    suspend fun getRunwayById(airportId: String, runwayId: String): Runway? {
        val response = runwaysService.getRunwayById(airportId, runwayId)
        response.body?.let {
            runwayDao.insertRunway(Runway.fromApiRunway(airportId, it))
        }
        return preferencesHelper.userId?.let { runwayDao.getRunwayById(it, airportId, runwayId) }
    }

    suspend fun createRunway(airportId: String, runwayRequest: RunwayRequest): String? {
        val response = runwaysService.postRunway(airportId, runwayRequest)
        response.body?.entityId?.let {
            getRunwayById(airportId, it)
        }
        return response.body?.entityId
    }

    suspend fun savaRunway(airportId: String, runwayId: String, runwayRequest: RunwayRequest) {
        runwaysService.putRunway(airportId, runwayId, runwayRequest)
        getRunwayById(airportId, runwayId)
    }

    suspend fun deleteRunway(airportId: String, runwayId: String) {
        runwaysService.deleteRunway(airportId, runwayId)
        preferencesHelper.userId?.let {
            val runway = runwayDao.getRunwayById(it, airportId, runwayId)
            runway?.let { entity ->
                runwayDao.delete(entity)
            }
        }
    }
}