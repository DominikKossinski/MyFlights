package pl.kossa.myflights.room.dao

import androidx.room.*
import pl.kossa.myflights.room.entities.*

@Dao
abstract class FlightDao(
    private val airplaneDao: AirplaneDao,
    private val airportDao: AirportDao,
    private val runwayDao: RunwayDao,
    private val ownerDataDao: OwnerDataDao,
    private val shareDataDao: ShareDataDao
) {

    @Transaction
    @Query("SELECT * FROM FlightModel")
    abstract suspend fun getAll(): List<Flight>

    @Query("SELECT * FROM FlightModel WHERE flightId = :flightId LIMIT 1")
    abstract suspend fun getFlightById(flightId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertFlightModel(flightModel: FlightModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertAirportModel(airportModel: FlightModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertAirplaneModel(airplaneModel: AirplaneModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertRunwayModel(runwayModel: RunwayModel)

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
}