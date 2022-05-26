package pl.kossa.myflights.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pl.kossa.myflights.room.entities.Airplane

@Dao
interface AirplaneDao {

    @Query("SELECT * FROM AirplaneModel WHERE userId = :userId")
    fun getAll(userId: String): Flow<List<Airplane>>
}