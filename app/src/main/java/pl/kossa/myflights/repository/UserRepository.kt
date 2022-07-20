package pl.kossa.myflights.repository

import pl.kossa.myflights.api.call.ApiResponse
import pl.kossa.myflights.api.models.User
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

    suspend fun getUser(): ResultWrapper<User?> {
        val response = makeRequest {
            userService.getUser()
        }
        if (response is ApiResponse.Success) {
            response.value?.let {
                preferencesHelper.setUserData(it)
            }
        }
        val user = preferencesHelper.getUser()
        return when (response) {
            is ApiResponse.Success -> {
                ResultWrapper.Success(user)
            }
            is ApiResponse.GenericError -> ResultWrapper.GenericError(user, response.apiError)
            is ApiResponse.NetworkError -> ResultWrapper.NetworkError(
                user,
                response.networkErrorType
            )
        }
    }

    suspend fun putUser(userRequest: UserRequest): ResultWrapper<Unit?> {
        val response = makeRequest {
            userService.putUser(userRequest)
        }
        return when (response) {
            is ApiResponse.Success -> ResultWrapper.Success(Unit)
            is ApiResponse.GenericError -> ResultWrapper.GenericError(null, response.apiError)
            is ApiResponse.NetworkError -> ResultWrapper.NetworkError(
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
            is ApiResponse.Success -> ResultWrapper.Success(Unit)
            is ApiResponse.GenericError -> ResultWrapper.GenericError(null, response.apiError)
            is ApiResponse.NetworkError -> ResultWrapper.NetworkError(
                null,
                response.networkErrorType
            )
        }
    }

    suspend fun deleteUser(): ResultWrapper<Unit?> {
        val response = makeRequest {
            userService.deleteUser()
        }
        return when (response) {
            is ApiResponse.Success -> {
                preferencesHelper.clearUserData()
                ResultWrapper.Success(Unit)
            }
            is ApiResponse.GenericError -> ResultWrapper.GenericError(null, response.apiError)
            is ApiResponse.NetworkError -> ResultWrapper.NetworkError(
                null,
                response.networkErrorType
            )
        }
    }

}