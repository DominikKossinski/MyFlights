package pl.kossa.myflights.repository

import pl.kossa.myflights.api.requests.AirportRequest
import pl.kossa.myflights.api.services.AirportsService
import pl.kossa.myflights.room.dao.AirportDao
import pl.kossa.myflights.room.entities.Airport
import pl.kossa.myflights.utils.PreferencesHelper

class AirportRepository(
    private val airportsService: AirportsService,
    private val airportDao: AirportDao,
    private val preferencesHelper: PreferencesHelper
) {

    suspend fun getAirports(): List<Airport> {
        val response = airportsService.getAirports()
        val airports = response.body ?: emptyList()
        airports.forEach {
            airportDao.insertAirport(Airport.fromApiAirport(it))
        }
        return preferencesHelper.userId?.let { airportDao.getAll(it) } ?: emptyList()
    }

    suspend fun getAirportById(airportId: String): Airport? {
        val response = airportsService.getAirportById(airportId)
        response.body?.let {
            airportDao.insertAirport(Airport.fromApiAirport(it))
        }
        return preferencesHelper.userId?.let { airportDao.getAirportById(it, airportId) }
    }

    suspend fun createAirport(airportRequest: AirportRequest): String? {
        val response = airportsService.postAirport(airportRequest)
        response.body?.entityId?.let {
            getAirportById(it)
        }
        return response.body?.entityId
    }

    suspend fun saveAirport(airportId: String, airportRequest: AirportRequest) {
        airportsService.putAirport(airportId, airportRequest)
        getAirportById(airportId)
    }

    suspend fun deleteAirport(airportId: String) {
        airportsService.deleteAirport(airportId)
        preferencesHelper.userId?.let {
            val airport = airportDao.getAirportById(it, airportId)
            airport?.let { entity ->
                airportDao.delete(entity)
            }
        }
    }
}