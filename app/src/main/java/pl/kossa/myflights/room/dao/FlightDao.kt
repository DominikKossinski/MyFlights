package pl.kossa.myflights.room.dao

import androidx.room.*
import pl.kossa.myflights.room.AppDatabase
import pl.kossa.myflights.room.entities.Flight
import pl.kossa.myflights.room.entities.FlightModel
import pl.kossa.myflights.room.entities.ImageModel

@Dao
abstract class FlightDao(
    db: AppDatabase
) {

    private val airplaneDao = db.getAirplaneDao()
    private val airportDao = db.getAirportDao()
    private val runwayDao = db.getRunwayDao()
    private val ownerDataDao = db.getOwnerDataDao()
    private val shareDataDao = db.getShareDataDao()

    @Transaction
    @Query("SELECT * FROM FlightModel")
    abstract suspend fun getAll(): List<Flight>

    @Query("SELECT * FROM FlightModel WHERE flightId = :flightId LIMIT 1")
    abstract suspend fun getFlightById(flightId: String): Flight

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertFlightModel(flightModel: FlightModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertImageModel(imageModel: ImageModel)

    suspend fun insertFlight(flight: Flight) {
        airplaneDao.insertAirplane(flight.airplane)
        airportDao.insertAirport(flight.arrivalAirport)
        airportDao.insertAirport(flight.departureAirport)
        runwayDao.insertRunway(flight.arrivalRunway)
        runwayDao.insertRunway(flight.departureRunway)
        ownerDataDao.insertOwnerData(flight.ownerData)
        flight.sharedUsers.forEach {
            shareDataDao.insertShareData(it)
        }
        insertFlightModel(flight.flight)
    }

    @Delete
    protected abstract suspend fun deleteFlightModel(flightModel: FlightModel)

    @Delete
    protected abstract suspend fun deleteImageModel(imageModel: ImageModel)

    suspend fun delete(userId: String, flight: Flight) {
        deleteFlightModel(flight.flight)
        // Deleting airplane
        val airplane = flight.airplane
        if (
            getAirplaneFlightsCount(airplane.airplane.airplaneId) == 0 &&
            airplane.airplane.userId != userId
        ) {
            airplaneDao.delete(flight.airplane)
        }
        val departureRunway = flight.departureRunway
        if (
            getRunwayFlightsCount(departureRunway.runway.runwayId) == 0 &&
            departureRunway.runway.userId != userId
        ) {
            runwayDao.delete(departureRunway)
        }
        val departureAirport = flight.departureAirport
        if (
            getAirportFlightsCount(departureAirport.airport.airportId) == 0 &&
            departureAirport.airport.userId != userId
        ) {
            airportDao.delete(departureAirport)
        }
        val arrivalRunway = flight.arrivalRunway
        if (
            getRunwayFlightsCount(arrivalRunway.runway.runwayId) == 0 &&
            arrivalRunway.runway.userId != userId
        ) {
            runwayDao.delete(arrivalRunway)
        }
        val arrivalAirport = flight.arrivalAirport
        if (
            getAirportFlightsCount(arrivalAirport.airport.airportId) == 0 &&
            arrivalAirport.airport.userId != userId
        ) {
            airportDao.delete(arrivalAirport)
        }
        // Deleting owner data
        if (
            getOwnerFlightsCount(flight.flight.ownerId) == 0 &&
            flight.flight.ownerId != userId
        ) {
            ownerDataDao.deleteOwnerData(flight.ownerData)
        }
        // Deleting shared users data
        flight.sharedUsers.forEach {
            shareDataDao.delete(it)
        }
    }

    @Query("SELECT COUNT(flightId) FROM FlightModel WHERE airplaneId = :airplaneId")
    protected abstract suspend fun getAirplaneFlightsCount(airplaneId: String): Int

    @Query("SELECT COUNT(flightId) FROM FlightModel WHERE departureRunwayId = :runwayId OR arrivalRunwayId = :runwayId")
    protected abstract suspend fun getRunwayFlightsCount(runwayId: String): Int

    @Query("SELECT COUNT(flightId) FROM FlightModel WHERE arrivalAirportId = :airportId OR arrivalAirportId = :airportId")
    protected abstract suspend fun getAirportFlightsCount(airportId: String): Int

    @Query("SELECT COUNT(flightId) FROM FlightModel WHERE ownerId = :ownerId")
    protected abstract suspend fun getOwnerFlightsCount(ownerId: String): Int
}