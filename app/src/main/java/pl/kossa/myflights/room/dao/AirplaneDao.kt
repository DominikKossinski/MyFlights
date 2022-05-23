package pl.kossa.myflights.room.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pl.kossa.myflights.api.models.Airplane

@Dao
interface AirplaneDao {

    @Query("SELECT * FROM Airplane WHERE airplane_user_id = :userId")
    fun getAll(userId: String): Flow<List<Airplane>>
}