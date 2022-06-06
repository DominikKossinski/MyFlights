package pl.kossa.myflights.room.dao

import androidx.room.*
import pl.kossa.myflights.room.entities.ShareData
import pl.kossa.myflights.room.entities.ShareDataModel

@Dao
abstract class ShareDataDao(
    private val sharedUserDataDao: SharedUserDataDao
) {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertShareDataModel(shareDataModel: ShareDataModel)

    suspend fun insertShareData(shareData: ShareData) {
        sharedUserDataDao.insertSharedUserData(shareData.sharedUserData)
        insertShareDataModel(shareData.sharedData)
    }

    @Delete
    protected abstract suspend fun deleteShareDataModel(shareDataModel: ShareDataModel)

    suspend fun delete(shareData: ShareData) {
        deleteShareDataModel(shareData.sharedData)
        if (getSharedFlightCount(shareData.sharedData.sharedUserId) == 0) {
            sharedUserDataDao.delete(shareData.sharedUserData)
        }
    }

    @Query("SELECT COUNT(sharedFlightId) From ShareDataModel WHERE sharedUserId = :sharedUserId")
    abstract suspend fun getSharedFlightCount(sharedUserId: String): Int
}