package pl.kossa.myflights.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.kossa.myflights.room.converters.DateConverter
import pl.kossa.myflights.room.entities.*

@Database(
    entities = [
        AirplaneModel::class, AirportModel::class, RunwayModel::class, FlightModel::class,
        ShareDataModel::class, ImageModel::class, SharedUserDataModel::class, OwnerDataModel::class
    ], version = 1
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
}