package pl.kossa.myflights.api.services

import pl.kossa.myflights.api.models.Flight
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FlightsService {

    @GET("/api/flights/{flightId}")
    suspend fun getFLightById(@Path("flightId") flightId: Int): Response<Flight>

    @GET("/api/flights")
    suspend fun getAllFlights(): Response<List<Flight>>

}