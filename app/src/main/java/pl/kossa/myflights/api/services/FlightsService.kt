package pl.kossa.myflights.api.services

import pl.kossa.myflights.api.call.ApiResponse
import pl.kossa.myflights.api.models.Flight
import pl.kossa.myflights.api.requests.FlightRequest
import pl.kossa.myflights.api.responses.CreatedResponse
import retrofit2.Response
import retrofit2.http.*

interface FlightsService {

    @GET("/api/flights/{flightId}")
    suspend fun getFLightById(@Path("flightId") flightId: String): ApiResponse<Flight>

    @GET("/api/flights")
    suspend fun getAllFlights(): ApiResponse<List<Flight>>

    @POST("/api/flights")
    suspend fun postFlight(@Body flightRequest: FlightRequest): ApiResponse<CreatedResponse>

    @PUT("/api/flights/{flightId}")
    suspend fun putFlight(
        @Path("flightId") flightId: String,
        @Body flightRequest: FlightRequest
    ): ApiResponse<Void>


    @DELETE("/api/flights/{flightId}")
    suspend fun deleteFlight(@Path("flightId") flightId: String): ApiResponse<Void>

}