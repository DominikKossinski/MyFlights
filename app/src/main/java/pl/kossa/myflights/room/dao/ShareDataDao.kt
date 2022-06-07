package pl.kossa.myflights.room.dao

import androidx.room.*
import pl.kossa.myflights.room.AppDatabase
import pl.kossa.myflights.room.entities.ShareData
import pl.kossa.myflights.room.entities.ShareDataModel

@Dao
abstract class ShareDataDao(
    db: AppDatabase
) {
    private val sharedUserDataDao = db.getSharedUserDataDao()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertShareDataModel(shareDataModel: ShareDataModel)

    suspend fun insertShareData(shareData: ShareData) {
        shareData.sharedUserData?.let { sharedUserDataDao.insertSharedUserData(it) }
        insertShareDataModel(shareData.sharedData)
    }

    @Delete
    protected abstract suspend fun deleteShareDataModel(shareDataModel: ShareDataModel)

    suspend fun delete(shareData: ShareData) {
        deleteShareDataModel(shareData.sharedData)
        if (shareData.sharedUserData != null &&getSharedFlightCount(shareData.sharedUserData.sharedUser.userId) == 0) {
            sharedUserDataDao.delete(shareData.sharedUserData)
        }
    }

    @Query("SELECT COUNT(sharedFlightId) From ShareDataModel WHERE sharedUserId = :sharedUserId")
    abstract suspend fun getSharedFlightCount(sharedUserId: String): Int
}