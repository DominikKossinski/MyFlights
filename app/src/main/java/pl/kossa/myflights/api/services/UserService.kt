package pl.kossa.myflights.api.services

import pl.kossa.myflights.api.models.User
import pl.kossa.myflights.api.requests.UserRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserService {

    @GET("/api/user")
    suspend fun getUser(): Response<User>

    @PUT("/api/user")
    suspend fun putUser(@Body userRequest: UserRequest): Response<Void>

    @DELETE("/api/user")
    suspend fun deleteUser() : Response<Void>
}