package pl.kossa.myflights.architecture

import com.google.firebase.crashlytics.FirebaseCrashlytics
import okhttp3.internal.http2.ConnectionShutdownException
import pl.kossa.myflights.api.call.ApiResponse1
import pl.kossa.myflights.api.exceptions.ApiServerException
import pl.kossa.myflights.api.exceptions.NoInternetException
import pl.kossa.myflights.api.exceptions.UnauthorizedException
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.ApiErrorBody
import pl.kossa.myflights.utils.PreferencesHelper
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class ResultWrapper<out T>(val value: T) {

    class Success<out T>(value: T) : ResultWrapper<T>(value)

    class GenericError<out T>(value: T, val apiError: ApiError) : ResultWrapper<T>(value)

    class NetworkError<out T>(value: T) : ResultWrapper<T>(value)
}

abstract class BaseRepository(
    protected val preferencesHelper: PreferencesHelper
) {

    protected suspend fun <T> makeRequest(
        block: suspend () -> ApiResponse1<T>
    ): ApiResponse1<T> {
        return try {
            block.invoke()
        } catch (e: UnauthorizedException) {
            return ApiResponse1.GenericError(ApiError(401, ApiErrorBody("mess", "desc")))
        } catch (e: NoInternetException) {
            return ApiResponse1.NetworkError
        } catch (e: ApiServerException) {
            return ApiResponse1.GenericError(ApiError(401, ApiErrorBody("mess", "desc")))
        } catch (e: Exception) {
            return when (e) {
                is SocketTimeoutException, is UnknownHostException, is ConnectionShutdownException, is IOException -> {
                    ApiResponse1.NetworkError
                }
                else -> {
                    // TODO crashlitics
                    FirebaseCrashlytics.getInstance().recordException(e)
                    return ApiResponse1.GenericError(ApiError(500, ApiErrorBody("mess", "desc")))
                }
            }
        }
    }

}