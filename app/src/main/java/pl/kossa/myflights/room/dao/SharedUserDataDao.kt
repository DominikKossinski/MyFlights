package pl.kossa.myflights.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import pl.kossa.myflights.room.entities.ImageModel
import pl.kossa.myflights.room.entities.SharedUserData
import pl.kossa.myflights.room.entities.SharedUserDataModel

@Dao
abstract class SharedUserDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertSharedUserDataModel(sharedUserDataModel: SharedUserDataModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertImageModel(imageModel: ImageModel)

    suspend fun insertSharedUserData(sharedUserData: SharedUserData) {
        sharedUserData.image?.let {
            insertImageModel(it)
        }
        insertSharedUserDataModel(sharedUserData.sharedUser)
    }

    @Delete
    protected abstract suspend fun deleteSharedUserDataModel(sharedUserDataModel: SharedUserDataModel)

    @Delete
    protected abstract suspend fun deleteImageModel(imageModel: ImageModel) //TODO think about it

    suspend fun delete(sharedUserData: SharedUserData) {
        deleteSharedUserDataModel(sharedUserData.sharedUser)
        sharedUserData.image?.let { deleteImageModel(it) }
    }
}