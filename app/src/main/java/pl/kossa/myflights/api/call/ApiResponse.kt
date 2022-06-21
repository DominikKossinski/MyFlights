package pl.kossa.myflights.api.call

import androidx.annotation.StringRes
import pl.kossa.myflights.R
import pl.kossa.myflights.api.responses.ApiError

data class ApiResponse<out T : Any>(val body: T?)

enum class NetworkErrorType(@StringRes val errorMessage: Int) {
    NO_INTERNET(R.string.error_no_internet),
    SERVER_CONNECTION_ERROR(R.string.error_no_connection_to_server)
}

sealed class ApiResponse1<out T> {

    data class Success<out T>(val value: T?) : ApiResponse1<T>()
    data class GenericError(val apiError: ApiError) : ApiResponse1<Nothing>()
    data class NetworkError(val networkErrorType: NetworkErrorType) : ApiResponse1<Nothing>()
}