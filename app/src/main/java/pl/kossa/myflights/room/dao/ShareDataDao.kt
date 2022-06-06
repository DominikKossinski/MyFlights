package pl.kossa.myflights.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import pl.kossa.myflights.room.entities.ImageModel
import pl.kossa.myflights.room.entities.ShareData
import pl.kossa.myflights.room.entities.ShareDataModel

@Dao
abstract class ShareDataDao(
    private val sharedUserDataDao: SharedUserDataDao
) {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertShareDataModel(shareDataModel: ShareDataModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertImageModel(imageModel: ImageModel)

    suspend fun insertShareData(shareData: ShareData) {
        sharedUserDataDao.insertSharedUserData(shareData.sharedUserData)
        insertShareDataModel(shareData.sharedData)
    }
}