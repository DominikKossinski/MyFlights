package pl.kossa.myflights.api.services

import pl.kossa.myflights.api.models.Flight
import pl.kossa.myflights.api.requests.FlightRequest
import pl.kossa.myflights.api.responses.CreatedResponse
import retrofit2.Response
import retrofit2.http.*

interface FlightsService {

    @GET("/api/flights/{flightId}")
    suspend fun getFLightById(@Path("flightId") flightId: String): Response<Flight>

    @GET("/api/flights")
    suspend fun getAllFlights(): Response<List<Flight>>

    @POST("/api/flights")
    suspend fun postFlight(@Body flightRequest: FlightRequest): Response<CreatedResponse>


    @DELETE("/api/flights/{flightId}")
    suspend fun deleteFlight(@Path("flightId") flightId: String): Response<Void>

}