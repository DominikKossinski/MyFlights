package pl.kossa.myflights.api.services

import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.api.requests.AirplaneRequest
import pl.kossa.myflights.api.responses.CreatedResponse
import retrofit2.Response
import retrofit2.http.*

interface AirplanesService {

    @GET("/api/airplanes")
    suspend fun getAirplanes(@Query("filter") filter: String = ""): Response<List<Airplane>>

    @GET("/api/airplanes/{airplaneId}")
    suspend fun getAirplaneById(@Path("airplaneId") airplaneId: String): Response<Airplane>

    @POST("/api/airplanes")
    suspend fun postAirplane(@Body airplaneRequest: AirplaneRequest): Response<CreatedResponse>

    @PUT("/api/airplanes/{airplaneId}")
    suspend fun putAirplane(
        @Path("airplaneId") airplaneId: String,
        @Body airplaneRequest: AirplaneRequest
    ): Response<Void>

    @DELETE("/api/airplanes/{airplaneId}")
    suspend fun deleteAirplane(@Path("airplaneId") airplaneId: String): Response<Void>
}