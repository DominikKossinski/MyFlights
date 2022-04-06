package pl.kossa.myflights.api.services

import pl.kossa.myflights.api.call.ApiResponse
import pl.kossa.myflights.api.models.SharedFlight
import pl.kossa.myflights.api.responses.CreatedResponse
import pl.kossa.myflights.api.responses.sharedflights.SharedFlightResponse
import pl.kossa.myflights.api.responses.sharedflights.SharedFlightJoinDetails
import retrofit2.http.*

interface SharedFlightsService {

    @GET("/api/shared-flights/")
    suspend fun getSharedFlights(): ApiResponse<List<SharedFlight>>

    @GET("/api/shared-flights/pending")
    suspend fun getPendingSharedFlights(): ApiResponse<List<SharedFlightResponse>>

    @GET("/api/shared-flights/{sharedFlightId}")
    suspend fun getSharedFlight(@Path("sharedFlightId") sharedFlightId: String): ApiResponse<SharedFlightResponse>

    @POST("/api/shared-flights/share/{flightId}")
    suspend fun shareFlight(@Path("flightId") flightId: String): ApiResponse<CreatedResponse>

    @PUT("/api/shared-flights/confirm/{sharedFlightId}")
    suspend fun confirmSharedFlight(@Path("sharedFlightId") sharedFlightId: String): ApiResponse<Void>

    @PUT("/api/shared-flights/join/{sharedFlightId}")
    suspend fun joinSharedFlight(@Path("sharedFlightId") sharedFlightId: String): ApiResponse<Void>

    @DELETE("/api/shared-flights/{sharedFlightId}")
    suspend fun deleteSharedFlight(@Path("sharedFlightId") sharedFlightId: String): ApiResponse<Void>

    @DELETE("/api/shared-flights/resign/{sharedFlightId}")
    suspend fun resignFromSharedFlight(@Path("sharedFlightId") sharedFlightId: String): ApiResponse<Void>

    @GET("/api/shared-flights/join/{sharedFlightId}")
    suspend fun getSharedFlightJoinDetails(@Path("sharedFlightId")sharedFlightId: String): ApiResponse<SharedFlightJoinDetails>
}