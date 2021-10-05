package pl.kossa.myflights.api.services

import pl.kossa.myflights.api.requests.RunwayRequest
import pl.kossa.myflights.api.responses.CreatedResponse
import retrofit2.Response
import retrofit2.http.*

interface RunwaysService {


    @POST("/api/airports/{airportId}/runways")
    suspend fun postRunway(
        @Path("airportId") airportId: String,
        @Body runwayRequest: RunwayRequest
    ): Response<CreatedResponse>

    @PUT("/api/airports/{airportId}/runways/{runwayId}")
    suspend fun putRunway(
        @Path("airportId") airportId: String,
        @Path("runwayId") runwayId: String,
        @Body runwayRequest: RunwayRequest
    ): Response<Void>

    @DELETE("/api/airports/{airportId}/runways/{runwayId}")
    suspend fun deleteRunway(
        @Path("airportId") airportId: String,
        @Path("runwayId") runwayId: String
    ): Response<Void>

}