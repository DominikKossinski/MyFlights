package pl.kossa.myflights.room.dao

import androidx.room.Dao
import pl.kossa.myflights.room.AppDatabase

@Dao
abstract class StatisticsDao(
    db: AppDatabase
) {

    private val airplaneDao = db.getAirplaneDao()
    private val airportDao = db.getAirportDao()
}