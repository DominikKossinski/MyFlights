package pl.kossa.myflights.repository

import pl.kossa.myflights.api.call.ApiResponse1
import pl.kossa.myflights.api.responses.StatisticsResponse
import pl.kossa.myflights.api.services.StatisticsService
import pl.kossa.myflights.architecture.BaseRepository
import pl.kossa.myflights.architecture.ResultWrapper
import pl.kossa.myflights.utils.PreferencesHelper

class StatisticsRepository(
    private val statisticsService: StatisticsService,
    preferencesHelper: PreferencesHelper
) : BaseRepository(preferencesHelper) {

    suspend fun getStatistics(): ResultWrapper<StatisticsResponse?> {
        val response = makeRequest {
            statisticsService.getStatistics()
        }
        return when (response) {
            is ApiResponse1.GenericError -> ResultWrapper.GenericError(null, response.apiError)
            is ApiResponse1.NetworkError -> ResultWrapper.NetworkError(
                null,
                response.networkErrorType
            )
            is ApiResponse1.Success -> ResultWrapper.Success(response.value)
        }
    }
}