package pl.kossa.myflights.api.services

import pl.kossa.myflights.api.call.ApiResponse
import pl.kossa.myflights.api.models.SharedFlight
import pl.kossa.myflights.api.responses.CreatedResponse
import retrofit2.http.*

interface SharedFlightsService {

    @GET("/api/shared-flights/")
    suspend fun getSharedFlights(): ApiResponse<List<SharedFlight>>

    @GET("/api/shared-flights/{sharedFlightId}")
    suspend fun getSharedFlight(@Path("sharedFlight") sharedFlightId: String): ApiResponse<SharedFlight>

    @POST("/api/shared-flights/share/{flightId}")
    suspend fun shareFlight(@Path("flightId") flightId: String): ApiResponse<CreatedResponse>

    @PUT("/api/shared-flights/confirm/{sharedFlightId}")
    suspend fun confirmSharedFlight(@Path("sharedFlightId") sharedFlightId: String): ApiResponse<Void>

    @PUT("/api/shared-flights/join/{sharedFlightId}")
    suspend fun joinSharedFlight(@Path("sharedFLightId") sharedFlightId: String): ApiResponse<Void>

    @DELETE("/api/shared-flights/{sharedFlightId}")
    suspend fun deleteSharedFlight(@Path("sharedFlightId") sharedFlightId: String): ApiResponse<Void>

    @DELETE("/api/shared-flights/resign/{sharedFlightId}")
    suspend fun resignFromSharedFlight(@Path("sharedFlight") sharedFlightId: String): ApiResponse<Void>
}