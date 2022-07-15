package pl.kossa.myflights.api.services

import pl.kossa.myflights.api.call.ApiResponse
import pl.kossa.myflights.api.responses.StatisticsResponse
import retrofit2.http.GET

interface StatisticsService {

    @GET("/api/statistics")
    suspend fun getStatistics(): ApiResponse<StatisticsResponse>
}