package pl.kossa.myflights.api.services

import pl.kossa.myflights.api.call.ApiResponse
import pl.kossa.myflights.api.call.ApiResponse1
import pl.kossa.myflights.api.requests.FlightRequest
import pl.kossa.myflights.api.responses.CreatedResponse
import pl.kossa.myflights.api.responses.flights.FlightResponse
import retrofit2.http.*

interface FlightsService {

    @GET("/api/flights/{flightId}")
    suspend fun getFLightById(@Path("flightId") flightId: String): ApiResponse1<FlightResponse>

    @GET("/api/flights")
    suspend fun getAllFlights(): ApiResponse1<List<FlightResponse>>

    @POST("/api/flights")
    suspend fun postFlight(@Body flightRequest: FlightRequest): ApiResponse1<CreatedResponse>

    @PUT("/api/flights/{flightId}")
    suspend fun putFlight(
        @Path("flightId") flightId: String,
        @Body flightRequest: FlightRequest
    ): ApiResponse1<Void>


    @DELETE("/api/flights/{flightId}")
    suspend fun deleteFlight(@Path("flightId") flightId: String): ApiResponse<Void>

}