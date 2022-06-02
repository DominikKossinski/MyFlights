package pl.kossa.myflights.room.dao

import androidx.room.*
import pl.kossa.myflights.room.entities.ImageModel
import pl.kossa.myflights.room.entities.Runway
import pl.kossa.myflights.room.entities.RunwayModel

@Dao
abstract class RunwayDao {

    @Transaction
    @Query("SELECT * FROM RunwayModel WHERE userId = :userId AND airportId = :airportId AND runwayId = :runwayId")
    abstract suspend fun getRunwayById(userId: String, airportId: String, runwayId: String): Runway?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertRunwayModel(runwayModel: RunwayModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertImageModel(imageModel: ImageModel)

    suspend fun insertRunway(runway: Runway) {
        runway.image?.let {
            insertImageModel(it)
        }
        insertRunwayModel(runway.runway)
    }

    @Delete
    protected abstract suspend fun deleteRunwayModel(runwayModel: RunwayModel)

    @Delete
    protected abstract suspend fun deleteImageModel(imageModel: ImageModel)

    suspend fun delete(runway: Runway) {
        runway.image?.let {
            deleteImageModel(it)
        }
        deleteRunwayModel(runway.runway)
    }
}