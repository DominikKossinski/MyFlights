package pl.kossa.myflights.repository

import pl.kossa.myflights.api.requests.FlightRequest
import pl.kossa.myflights.api.services.FlightsService
import pl.kossa.myflights.room.dao.FlightDao
import pl.kossa.myflights.room.entities.Flight
import pl.kossa.myflights.utils.PreferencesHelper

class FlightRepository(
    private val flightsService: FlightsService,
    private val flightDao: FlightDao,
    private val preferencesHelper: PreferencesHelper
) {

    suspend fun getFlights(): List<Flight> {
        val response = flightsService.getAllFlights()
        val flights = response.body ?: emptyList()
        flights.forEach {
            flightDao.insertFlight(Flight.fromApiFlight(it))
        }
        return flightDao.getAll()
    }

    suspend fun getFlightById(flightId: String): Flight? {
        val response = flightsService.getFLightById(flightId)
        response.body?.let {
            flightDao.insertFlight(Flight.fromApiFlight(it))
        }
        return flightDao.getFlightById(flightId)
    }

    suspend fun createFlight(flightRequest: FlightRequest): String? {
        val response = flightsService.postFlight(flightRequest)
        response.body?.entityId?.let {
            getFlightById(it)
        }
        return response.body?.entityId
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