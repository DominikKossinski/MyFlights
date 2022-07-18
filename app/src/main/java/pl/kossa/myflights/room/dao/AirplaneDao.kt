package pl.kossa.myflights.room.dao

import androidx.room.*
import pl.kossa.myflights.room.entities.Airplane
import pl.kossa.myflights.room.entities.AirplaneModel
import pl.kossa.myflights.room.entities.ImageModel

@Dao
abstract class AirplaneDao {

    @Transaction
    @Query("SELECT * FROM AirplaneModel WHERE userId = :userId AND name LIKE '%' || :text || '%'")
    abstract suspend fun getAll(userId: String, text: String): List<Airplane>

    @Transaction
    @Query("SELECT * FROM AirplaneModel WHERE userId = :userId AND airplaneId = :airplaneId LIMIT 1")
    abstract suspend fun getAirplaneById(userId: String, airplaneId: String): Airplane?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertAirplaneModel(airplaneModel: AirplaneModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertImageModel(imageModel: ImageModel)

    suspend fun insertAirplane(airplane: Airplane) {
        airplane.image?.let {
            insertImageModel(it)
        }
        insertAirplaneModel(airplane.airplane)
    }

    @Delete
    protected abstract suspend fun deleteAirplaneModel(airplaneModel: AirplaneModel)

    @Delete
    protected abstract suspend fun deleteImageModel(imageModel: ImageModel)

    suspend fun delete(airplane: Airplane) {
        airplane.image?.let {
            deleteImageModel(it)
        }
        deleteAirplaneModel(airplane.airplane)
    }
}