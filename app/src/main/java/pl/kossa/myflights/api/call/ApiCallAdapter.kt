package pl.kossa.myflights.api.call

import okhttp3.ResponseBody
import pl.kossa.myflights.api.responses.ApiErrorBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import java.lang.reflect.Type

class ApiCallAdapter<S : Any>(
    private val successType: Type,
    private val errorBodyConverter: Converter<ResponseBody, ApiErrorBody>
) : CallAdapter<S, Call<ApiResponse<S>>> {
    override fun responseType(): Type = successType

    override fun adapt(call: Call<S>): Call<ApiResponse<S>> {
        return ApiCall(call, errorBodyConverter)
    }
}