package pl.kossa.myflights.repository

import pl.kossa.myflights.api.requests.FcmRequest
import pl.kossa.myflights.api.requests.UserRequest
import pl.kossa.myflights.api.services.UserService

class UserRepository(
    private val userService: UserService
) {

    suspend fun getUser() = userService.getUser()

    suspend fun putUser(userRequest: UserRequest) = userService.putUser(userRequest)

    suspend fun putFcmToken(fcmRequest: FcmRequest) = userService.putFcmToken(fcmRequest)

    suspend fun deleteUser() = userService.deleteUser()

}