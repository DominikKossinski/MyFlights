package pl.kossa.myflights.room

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.kossa.myflights.room.entities.*

@Database(
    entities = [
        AirplaneModel::class, AirportModel::class, RunwayModel::class, FlightModel::class, ImageModel::class
    ], version = 1
)
abstract class AppDatabase : RoomDatabase() {
}