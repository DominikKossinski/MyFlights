package pl.kossa.myflights.repository

import pl.kossa.myflights.api.services.StatisticsService

class StatisticsRepository(
    private val statisticsService: StatisticsService
) {

    suspend fun getStatistics() = statisticsService.getStatistics()
}