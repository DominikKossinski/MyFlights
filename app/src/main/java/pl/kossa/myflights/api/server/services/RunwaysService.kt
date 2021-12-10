package pl.kossa.myflights.api.server.services

import pl.kossa.myflights.api.call.ApiResponse
import pl.kossa.myflights.api.server.models.Runway
import pl.kossa.myflights.api.server.requests.RunwayRequest
import pl.kossa.myflights.api.server.responses.CreatedResponse
import retrofit2.http.*

interface RunwaysService {

    @GET("/api/airports/{airportId}/runways/{runwayId}")
    suspend fun getRunwayById(
        @Path("airportId") airportId: String,
        @Path("runwayId") runwayId: String
    ): ApiResponse<Runway>

    @POST("/api/airports/{airportId}/runways")
    suspend fun postRunway(
        @Path("airportId") airportId: String,
        @Body runwayRequest: RunwayRequest
    ): ApiResponse<CreatedResponse>

    @PUT("/api/airports/{airportId}/runways/{runwayId}")
    suspend fun putRunway(
        @Path("airportId") airportId: String,
        @Path("runwayId") runwayId: String,
        @Body runwayRequest: RunwayRequest
    ): ApiResponse<Void>

    @DELETE("/api/airports/{airportId}/runways/{runwayId}")
    suspend fun deleteRunway(
        @Path("airportId") airportId: String,
        @Path("runwayId") runwayId: String
    ): ApiResponse<Void>

}