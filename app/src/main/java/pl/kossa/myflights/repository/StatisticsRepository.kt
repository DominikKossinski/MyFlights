package pl.kossa.myflights.repository

import pl.kossa.myflights.api.call.ApiResponse
import pl.kossa.myflights.api.responses.HttpCode
import pl.kossa.myflights.api.services.StatisticsService
import pl.kossa.myflights.architecture.BaseRepository
import pl.kossa.myflights.architecture.ResultWrapper
import pl.kossa.myflights.room.dao.StatisticsDao
import pl.kossa.myflights.room.entities.Statistics
import pl.kossa.myflights.utils.PreferencesHelper

class StatisticsRepository(
    private val statisticsService: StatisticsService,
    private val statisticsDao: StatisticsDao,
    preferencesHelper: PreferencesHelper
) : BaseRepository(preferencesHelper) {

    suspend fun getStatistics(): ResultWrapper<Statistics?> {
        val response = makeRequest {
            statisticsService.getStatistics()
        }
        if (response is ApiResponse.Success) {
            response.value?.let { stats ->
                preferencesHelper.userId?.let {
                    statisticsDao.insertStatistics(Statistics.fromApiStatisticsResponse(it, stats))
                }
            }
        }
        val statistics = preferencesHelper.userId?.let { statisticsDao.getUserStatistics(it) }
        return when (response) {
            is ApiResponse.Success -> ResultWrapper.Success(statistics)
            is ApiResponse.GenericError -> {
                if (response.apiError.code == HttpCode.NOT_FOUND.code) {
                    preferencesHelper.userId?.let {
                        statisticsDao.deleteStatisticsByUserId(it)
                    }
                    ResultWrapper.GenericError(null, response.apiError)
                } else {
                    ResultWrapper.GenericError(statistics, response.apiError)
                }
            }
            is ApiResponse.NetworkError -> ResultWrapper.NetworkError(
                statistics,
                response.networkErrorType
            )
        }
    }
}