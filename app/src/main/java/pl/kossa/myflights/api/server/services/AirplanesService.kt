package pl.kossa.myflights.api.server.services

import pl.kossa.myflights.api.call.ApiResponse
import pl.kossa.myflights.api.server.models.Airplane
import pl.kossa.myflights.api.server.requests.AirplaneRequest
import pl.kossa.myflights.api.server.responses.CreatedResponse
import retrofit2.http.*

interface AirplanesService {

    @GET("/api/airplanes")
    suspend fun getAirplanes(@Query("filter") filter: String = ""): ApiResponse<List<Airplane>>

    @GET("/api/airplanes/{airplaneId}")
    suspend fun getAirplaneById(@Path("airplaneId") airplaneId: String): ApiResponse<Airplane>

    @POST("/api/airplanes")
    suspend fun postAirplane(@Body airplaneRequest: AirplaneRequest): ApiResponse<CreatedResponse>

    @PUT("/api/airplanes/{airplaneId}")
    suspend fun putAirplane(
        @Path("airplaneId") airplaneId: String,
        @Body airplaneRequest: AirplaneRequest
    ): ApiResponse<Void>

    @DELETE("/api/airplanes/{airplaneId}")
    suspend fun deleteAirplane(@Path("airplaneId") airplaneId: String): ApiResponse<Void>
}