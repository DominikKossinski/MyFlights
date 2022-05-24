package pl.kossa.myflights.room

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.kossa.myflights.room.entities.AirplaneModel
import pl.kossa.myflights.room.entities.ImageModel

@Database(entities = [AirplaneModel::class, ImageModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
}