package pl.kossa.myflights.api.services

import pl.kossa.myflights.api.call.ApiResponse
import pl.kossa.myflights.api.call.ApiResponse1
import pl.kossa.myflights.api.models.Runway
import pl.kossa.myflights.api.requests.RunwayRequest
import pl.kossa.myflights.api.responses.CreatedResponse
import retrofit2.Response
import retrofit2.http.*

interface RunwaysService {

    @GET("/api/airports/{airportId}/runways/{runwayId}")
    suspend fun getRunwayById(
        @Path("airportId") airportId: String,
        @Path("runwayId") runwayId: String
    ): ApiResponse1<Runway>

    @POST("/api/airports/{airportId}/runways")
    suspend fun postRunway(
        @Path("airportId") airportId: String,
        @Body runwayRequest: RunwayRequest
    ): ApiResponse1<CreatedResponse>

    @PUT("/api/airports/{airportId}/runways/{runwayId}")
    suspend fun putRunway(
        @Path("airportId") airportId: String,
        @Path("runwayId") runwayId: String,
        @Body runwayRequest: RunwayRequest
    ): ApiResponse1<Void>

    @DELETE("/api/airports/{airportId}/runways/{runwayId}")
    suspend fun deleteRunway(
        @Path("airportId") airportId: String,
        @Path("runwayId") runwayId: String
    ): ApiResponse<Void>

}