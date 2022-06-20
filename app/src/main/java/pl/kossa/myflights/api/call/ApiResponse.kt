package pl.kossa.myflights.api.call

import pl.kossa.myflights.api.responses.ApiError

data class ApiResponse<out T : Any>(val body: T?)

sealed class ApiResponse1<out T> {

    data class Success<out T>(val value: T?) : ApiResponse1<T>()
    data class GenericError(val apiError: ApiError) : ApiResponse1<Nothing>()
    object NetworkError : ApiResponse1<Nothing>()
}