package pl.kossa.myflights.api.services

import pl.kossa.myflights.api.call.ApiResponse
import pl.kossa.myflights.api.call.ApiResponse1
import pl.kossa.myflights.api.models.User
import pl.kossa.myflights.api.requests.FcmRequest
import pl.kossa.myflights.api.requests.UserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserService {

    @GET("/api/user")
    suspend fun getUser(): ApiResponse<User>

    @PUT("/api/user")
    suspend fun putUser(@Body userRequest: UserRequest): ApiResponse<Void>

    @PUT("/api/user/fcm")
    suspend fun putFcmToken(@Body fcmRequest: FcmRequest): ApiResponse1<Void>

    @DELETE("/api/user")
    suspend fun deleteUser() : ApiResponse<Void>
}