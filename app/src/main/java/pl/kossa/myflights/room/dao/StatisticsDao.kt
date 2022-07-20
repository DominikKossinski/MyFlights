package pl.kossa.myflights.room.dao

import androidx.room.*
import pl.kossa.myflights.room.AppDatabase
import pl.kossa.myflights.room.entities.*

@Dao
abstract class StatisticsDao(
    db: AppDatabase
) {

    private val airplaneDao = db.getAirplaneDao()
    private val airportDao = db.getAirportDao()

    @Transaction
    @Query("SELECT * FROM TopNAirportModel WHERE userId = :userId AND isDeparture = :isDeparture")
    protected abstract suspend fun getTopNAirports(
        userId: String,
        isDeparture: Boolean
    ): List<TopNAirport>


    @Transaction
    @Query("SELECT * FROM StatisticsModel WHERE userId = :userId")
    protected abstract suspend fun getStatisticsByUserId(userId: String): Statistics?

    suspend fun getUserStatistics(userId: String): Statistics? {
        var statistics = getStatisticsByUserId(userId)
        statistics = statistics?.copy(top5DepartureAirports = getTopNAirports(userId, true))
        statistics = statistics?.copy(top5ArrivalAirports = getTopNAirports(userId, false))
        return statistics
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertStatisticsModel(statisticsModel: StatisticsModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertTopNAirplaneModel(topNAirplaneModel: TopNAirplaneModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract fun insertTopNAirportModel(topNAirportModel: TopNAirportModel)

    suspend fun insertStatistics(statistics: Statistics) {
        statistics.favouriteAirplane?.let {
            airplaneDao.insertAirplane(it)
        }
        statistics.top5Airplanes.forEach {
            airplaneDao.insertAirplane(it.airplane)
            insertTopNAirplaneModel(it.topNAirplane)
        }
        statistics.favouriteDepartureAirport?.let {
            airportDao.insertAirport(it)
        }
        statistics.top5DepartureAirports.forEach {
            airportDao.insertAirport(it.airport)
            insertTopNAirportModel(it.topNAirport)
        }
        statistics.favouriteArrivalAirport?.let {
            airportDao.insertAirport(it)
        }
        statistics.top5ArrivalAirports.forEach {
            airportDao.insertAirport(it.airport)
            insertTopNAirportModel(it.topNAirport)
        }
        insertStatisticsModel(statistics.statistics)
    }

    @Delete
    protected abstract suspend fun deleteStatisticsModel(statisticsModel: StatisticsModel)

    @Delete
    protected abstract suspend fun deleteTopNAirplaneModel(topNAirplaneModel: TopNAirplaneModel)

    @Delete
    protected abstract suspend fun deleteTopNAirportModel(topNAirportModel: TopNAirportModel)

    @Delete
    suspend fun deleteStatisticsByUserId(userId: String) {
        val statistics = getUserStatistics(userId)
        statistics?.let {
            it.top5Airplanes.forEach { airplane ->
                deleteTopNAirplaneModel(airplane.topNAirplane)
            }
            it.top5DepartureAirports.forEach { airport ->
                deleteTopNAirportModel(airport.topNAirport)
            }
            it.top5ArrivalAirports.forEach { airport ->
                deleteTopNAirportModel(airport.topNAirport)
            }
            deleteStatisticsModel(it.statistics)
        }
    }
}