package pl.kossa.myflights.api

import android.util.Log
import okhttp3.OkHttpClient
import pl.kossa.myflights.api.services.AirplanesService
import pl.kossa.myflights.utils.PreferencesHelper
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiService(
    private val preferencesHelper: PreferencesHelper
) {

    private val client = OkHttpClient.Builder().addInterceptor { chain ->
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${preferencesHelper.token}")
            .build()

        Log.d("MyLog", "$newRequest")
        chain.proceed(newRequest)
    }.build()


    private val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("http://10.0.2.2:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: MyFlightsService = retrofit.create(MyFlightsService::class.java)

    val airplanesService: AirplanesService = retrofit.create(AirplanesService::class.java)


//        fun getUser() {
//            GlobalScope.launch {
//            try {
//                Log.d("MyLog", "Getting")
//                val response = service.getHello()
//                Log.d("MyLog", "${response.body()}")
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }
}
