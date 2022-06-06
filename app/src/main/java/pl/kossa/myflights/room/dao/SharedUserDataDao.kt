package pl.kossa.myflights.room.dao

import androidx.room.Dao
import androidx.room.Insert
import pl.kossa.myflights.room.entities.ImageModel
import pl.kossa.myflights.room.entities.SharedUserData
import pl.kossa.myflights.room.entities.SharedUserDataModel

@Dao
abstract class SharedUserDataDao {

    @Insert
    protected abstract suspend fun insertSharedUserDataModel(sharedUserDataModel: SharedUserDataModel)

    @Insert
    protected abstract suspend fun insertImageModel(imageModel: ImageModel)

    suspend fun insertSharedUserData(sharedUserData: SharedUserData) {
        sharedUserData.image?.let {
            insertImageModel(it)
        }
        insertSharedUserDataModel(sharedUserData.sharedUser)
    }
}