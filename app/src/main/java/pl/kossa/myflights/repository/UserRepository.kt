package pl.kossa.myflights.repository

import pl.kossa.myflights.api.call.ApiResponse1
import pl.kossa.myflights.api.requests.FcmRequest
import pl.kossa.myflights.api.requests.UserRequest
import pl.kossa.myflights.api.services.UserService
import pl.kossa.myflights.architecture.BaseRepository
import pl.kossa.myflights.architecture.ResultWrapper
import pl.kossa.myflights.utils.PreferencesHelper

class UserRepository(
    private val userService: UserService,
    preferencesHelper: PreferencesHelper
) : BaseRepository(preferencesHelper) {

    suspend fun getUser() = userService.getUser()

    suspend fun putUser(userRequest: UserRequest): ResultWrapper<Unit?> {
        val response = makeRequest {
            userService.putUser(userRequest)
        }
        return when (response) {
            is ApiResponse1.Success -> ResultWrapper.Success(Unit)
            is ApiResponse1.GenericError -> ResultWrapper.GenericError(null, response.apiError)
            is ApiResponse1.NetworkError -> ResultWrapper.NetworkError(
                null,
                response.networkErrorType
            )
        }
    }

    suspend fun putFcmToken(fcmRequest: FcmRequest): ResultWrapper<Unit?> {
        val response = makeRequest {
            userService.putFcmToken(fcmRequest)
        }
        return when (response) {
            is ApiResponse1.Success -> ResultWrapper.Success(Unit)
            is ApiResponse1.GenericError -> ResultWrapper.GenericError(null, response.apiError)
            is ApiResponse1.NetworkError -> ResultWrapper.NetworkError(
                null,
                response.networkErrorType
            )
        }
    }

    suspend fun deleteUser(): ResultWrapper<Unit?> {
        val response = makeRequest {
            userService.deleteUser()
        }
        return when(response) {
            is ApiResponse1.Success -> {
                preferencesHelper.clearUserData()
                ResultWrapper.Success(Unit)
            }
            is ApiResponse1.GenericError -> ResultWrapper.GenericError(null, response.apiError)
            is ApiResponse1.NetworkError -> ResultWrapper.NetworkError(null, response.networkErrorType)
        }
    }

}