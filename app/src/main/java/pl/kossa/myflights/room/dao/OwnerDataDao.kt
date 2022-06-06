package pl.kossa.myflights.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import pl.kossa.myflights.room.entities.ImageModel
import pl.kossa.myflights.room.entities.OwnerData
import pl.kossa.myflights.room.entities.OwnerDataModel

@Dao
abstract class OwnerDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertOwnerDataModel(ownerDataModel: OwnerDataModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertImageModel(imageModel: ImageModel)

    suspend fun insertOwnerData(ownerData: OwnerData) {
        ownerData.image?.let {
            insertImageModel(it)
        }
        insertOwnerDataModel(ownerData.ownerData)
    }

    @Delete
    protected abstract suspend fun deleteOwnerDataModel(ownerDataModel: OwnerDataModel)

    @Delete
    protected abstract suspend fun deleteImageModel(imageModel: ImageModel)

    suspend fun deleteOwnerData(ownerData: OwnerData) {
        deleteOwnerDataModel(ownerData.ownerData)
        ownerData.image?.let { deleteImageModel(it) }
    }
}