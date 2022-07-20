package pl.kossa.myflights.room.dao

import androidx.room.*
import pl.kossa.myflights.room.entities.Airport
import pl.kossa.myflights.room.entities.AirportModel
import pl.kossa.myflights.room.entities.ImageModel
import pl.kossa.myflights.room.entities.RunwayModel

@Dao
abstract class AirportDao {

    @Transaction
    @Query("SELECT * FROM AirportModel WHERE userId = :userId AND (name LIKE '%' || :text || '%' OR city LIKE '%' || :text || '%' OR icaoCode LIKE '%' || :text || '%')")
    abstract suspend fun getAll(userId: String, text: String): List<Airport>

    @Transaction
    @Query("SELECT * FROM AirportModel WHERE userId = :userId AND airportId = :airportId")
    abstract suspend fun getAirportById(userId: String, airportId: String): Airport?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertAirportModel(airportModel: AirportModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertRunwayModel(runwayModel: RunwayModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertImageModel(imageModel: ImageModel)

    suspend fun insertAirport(airport: Airport) {
        airport.image?.let {
            insertImageModel(it)
        }
        airport.runways.forEach {
            it.image?.let { imageModel -> insertImageModel(imageModel) }
            insertRunwayModel(it.runway)
        }
        insertAirportModel(airport.airport)
    }

    @Delete
    protected abstract suspend fun deleteAirportModel(airportModel: AirportModel)

    @Delete
    protected abstract suspend fun deleteRunwayModel(runwayModel: RunwayModel)

    @Delete
    protected abstract suspend fun deleteImageModel(imageModel: ImageModel)

    suspend fun delete(airport: Airport) {
        airport.image?.let {
            deleteImageModel(it)
        }
        airport.runways.forEach {
            it.image?.let { imageModel -> deleteImageModel(imageModel) }
            deleteRunwayModel(it.runway)
        }
        deleteAirportModel(airport.airport)
    }
}