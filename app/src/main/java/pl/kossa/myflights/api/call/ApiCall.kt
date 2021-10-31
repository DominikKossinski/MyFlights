package pl.kossa.myflights.api.call

import android.util.Log
import okhttp3.Request
import okhttp3.ResponseBody
import pl.kossa.myflights.api.exceptions.ApiServerException
import pl.kossa.myflights.api.exceptions.UnauthorizedException
import pl.kossa.myflights.api.responses.ApiError
import pl.kossa.myflights.api.responses.ApiErrorBody
import pl.kossa.myflights.api.responses.HttpCode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException
import java.lang.UnsupportedOperationException

class ApiCall<S : Any>(
    private val delegate: Call<S>,
    private val errorConverter: Converter<ResponseBody, ApiErrorBody>
) : Call<ApiResponse<S>> {

    override fun enqueue(callback: Callback<ApiResponse<S>>) {
        return delegate.enqueue(object : Callback<S> {
            override fun onResponse(call: Call<S>, response: Response<S>) {
                Log.d("MyLog", "OnResponse $response")
                if (response.isSuccessful) {
                    callback.onResponse(
                        this@ApiCall,
                        Response.success(ApiResponse(response.body()))
                    )
                } else {
                    if (response.code() == HttpCode.UNAUTHORIZED.code) {
                        callback.onFailure(this@ApiCall, UnauthorizedException())
                    } else {
                        val apiError = try {
                            val apiErrorBody = response.errorBody()?.let { errorConverter.convert(it) }
                            ApiError(response.code(), apiErrorBody)
                        } catch (e: IOException) {
                            ApiError(HttpCode.INTERNAL_SERVER_ERROR.code, null)
                        }
                        callback.onFailure(this@ApiCall, ApiServerException(apiError))
                    }
                }
            }

            override fun onFailure(call: Call<S>, t: Throwable) {
                callback.onFailure(this@ApiCall, t)
            }

        })
    }


    override fun isExecuted() = delegate.isExecuted

    override fun clone() = ApiCall(delegate.clone(), errorConverter)

    override fun isCanceled() = delegate.isCanceled

    override fun cancel() = delegate.cancel()

    override fun execute(): Response<ApiResponse<S>> {
        throw UnsupportedOperationException("Api does not support execute")
    }

    override fun request(): Request = delegate.request()
}