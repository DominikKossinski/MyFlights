package pl.kossa.myflights.api.services

import pl.kossa.myflights.api.call.ApiResponse
import pl.kossa.myflights.api.call.ApiResponse1
import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.api.requests.AirplaneRequest
import pl.kossa.myflights.api.responses.CreatedResponse
import retrofit2.http.*

interface AirplanesService {

    @GET("/api/airplanes")
    suspend fun getAirplanes(@Query("filter") filter: String = ""): ApiResponse1<List<Airplane>>

    @GET("/api/airplanes/{airplaneId}")
    suspend fun getAirplaneById(@Path("airplaneId") airplaneId: String): ApiResponse1<Airplane>

    @POST("/api/airplanes")
    suspend fun postAirplane(@Body airplaneRequest: AirplaneRequest): ApiResponse1<CreatedResponse>

    @PUT("/api/airplanes/{airplaneId}")
    suspend fun putAirplane(
        @Path("airplaneId") airplaneId: String,
        @Body airplaneRequest: AirplaneRequest
    ): ApiResponse1<Void>

    @DELETE("/api/airplanes/{airplaneId}")
    suspend fun deleteAirplane(@Path("airplaneId") airplaneId: String): ApiResponse<Void>
}