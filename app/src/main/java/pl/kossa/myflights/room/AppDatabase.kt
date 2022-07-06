package pl.kossa.myflights.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.kossa.myflights.room.converters.DateConverter
import pl.kossa.myflights.room.dao.*
import pl.kossa.myflights.room.entities.*

@Database(
    entities = [
        AirplaneModel::class, AirportModel::class, RunwayModel::class, FlightModel::class,
        ShareDataModel::class, ImageModel::class, SharedUserDataModel::class, OwnerDataModel::class,
        StatisticsModel::class, TopNAirportModel::class, TopNAirplaneModel::class
    ], version = 1
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getAirplaneDao(): AirplaneDao

    abstract fun getAirportDao(): AirportDao

    abstract fun getFlightDao(): FlightDao

    abstract fun getRunwayDao(): RunwayDao

    abstract fun getOwnerDataDao(): OwnerDataDao

    abstract fun getShareDataDao(): ShareDataDao

    abstract fun getSharedUserDataDao(): SharedUserDataDao
}