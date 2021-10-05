package pl.kossa.myflights.api.services

import pl.kossa.myflights.api.models.Airport
import pl.kossa.myflights.api.requests.AirportRequest
import pl.kossa.myflights.api.responses.CreatedResponse
import retrofit2.Response
import retrofit2.http.*

interface AirportsService {


    @GET("/api/airports")
    suspend fun getAirports(@Query("filter") filter: String = ""): Response<List<Airport>>

    @GET("/api/airports/{airportId}")
    suspend fun getAirportById(@Path("airportId") airportId: String): Response<Airport>

    @POST("/api/airports")
    suspend fun postAirport(@Body airportRequest: AirportRequest): Response<CreatedResponse>

    @PUT("/api/airports/{airportId}")
    suspend fun putAirport(
        @Path("airportId") airportId: String,
        @Body airportRequest: AirportRequest
    ): Response<Void>

    @DELETE("/api/airports/{airportId}")
    suspend fun deleteAirport(@Path("airportId") airportId: String): Response<Void>

}