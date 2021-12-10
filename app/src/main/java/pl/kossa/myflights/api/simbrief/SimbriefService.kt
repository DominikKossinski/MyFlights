package pl.kossa.myflights.api.simbrief

import pl.kossa.myflights.api.call.ApiResponse
import pl.kossa.myflights.api.simbrief.models.OFP
import retrofit2.http.GET
import retrofit2.http.Path

interface SimbriefService {

    @GET("/ofp/flightplans/xml/{timestamp}_{md5}.xml")
    suspend fun getFlightPlan(@Path("timestamp") timestamp: Long, @Path("md5") md5: String) : ApiResponse<OFP>
}