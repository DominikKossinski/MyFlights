package pl.kossa.myflights.api

import pl.kossa.myflights.api.models.Flight
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MyFlightsService {

    @GET("/api/flights")
    suspend fun getAllFlights(): Response<List<Flight>>

    @GET("/api/flights/{flightId}")
    suspend fun getFLightById(@Path("flightId") flightId: Int): Response<Flight>
}