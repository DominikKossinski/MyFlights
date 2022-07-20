package pl.kossa.myflights.api.call

import androidx.annotation.StringRes
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.ApiError


enum class NetworkErrorType(@StringRes val errorMessage: Int) {
    NO_INTERNET(R.string.error_no_internet),
    SERVER_CONNECTION_ERROR(R.string.error_no_connection_to_server)
}

sealed class ApiResponse<out T> {

    data class Success<out T>(val value: T?) : ApiResponse<T>()
    data class GenericError(val apiError: ApiError) : ApiResponse<Nothing>()
    data class NetworkError(val networkErrorType: NetworkErrorType) : ApiResponse<Nothing>()
}