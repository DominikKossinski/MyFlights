package pl.kossa.myflights.api.server.services

import pl.kossa.myflights.api.call.ApiResponse
import pl.kossa.myflights.api.server.models.User
import pl.kossa.myflights.api.server.requests.FcmRequest
import pl.kossa.myflights.api.server.requests.UserRequest
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
    suspend fun putFcmToken(@Body fcmRequest: FcmRequest): ApiResponse<Void>

    @DELETE("/api/user")
    suspend fun deleteUser() : ApiResponse<Void>
}