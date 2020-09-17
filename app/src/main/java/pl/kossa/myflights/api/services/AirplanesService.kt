package pl.kossa.myflights.api.services

import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.api.requests.AirplaneRequest
import pl.kossa.myflights.api.responses.CreatedResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AirplanesService {

    @GET("/api/airplanes")
    suspend fun getAllAirplanes(): Response<List<Airplane>>

    @GET("/api/airplanes/{airplaneId}")
    suspend fun getAirportById(@Path("airplaneId") airplaneId: Int): Response<Airplane>

    @POST("/api/airplanes")
    suspend fun postAirplane(@Body airplaneRequest: AirplaneRequest): Response<CreatedResponse>
}