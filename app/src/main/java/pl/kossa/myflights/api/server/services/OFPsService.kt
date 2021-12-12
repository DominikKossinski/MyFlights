package pl.kossa.myflights.api.server.services

import pl.kossa.myflights.api.call.ApiResponse
import pl.kossa.myflights.api.server.responses.CreatedResponse
import pl.kossa.myflights.api.simbrief.models.OFP
import retrofit2.http.Body
import retrofit2.http.POST

interface OFPsService {

    @POST("/api/ofps")
    suspend fun postOFP(@Body ofp: OFP): ApiResponse<CreatedResponse>
}