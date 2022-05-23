package pl.kossa.myflights.room

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.kossa.myflights.api.models.Airplane

@Database(entities = [Airplane::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
}