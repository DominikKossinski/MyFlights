package pl.kossa.myflights.hilt

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import pl.kossa.myflights.api.models.Runway
import pl.kossa.myflights.api.services.AirplanesService
import pl.kossa.myflights.api.services.AirportsService
import pl.kossa.myflights.api.services.FlightsService
import pl.kossa.myflights.api.services.RunwaysService
import pl.kossa.myflights.utils.PreferencesHelper
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun preferencesHelper(@ApplicationContext applicationContext: Context): PreferencesHelper {
        return PreferencesHelper(applicationContext)
    }

    @Provides
    fun provideOkHttpClient(preferencesHelper: PreferencesHelper): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${preferencesHelper.token}")
                .build()
            Log.d("MyLog", "Token: ${preferencesHelper.token}")
//        TODO handling errors
            Log.d("MyLog", "$newRequest")
            chain.proceed(newRequest)
        }.build()
    }

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().apply {
            setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        }.create()
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl("http://10.0.2.2:8080/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun provideFlightsService(retrofit: Retrofit): FlightsService {
        return retrofit.create(FlightsService::class.java)
    }

    @Provides
    fun provideAirportsService(retrofit: Retrofit): AirportsService {
        return retrofit.create(AirportsService::class.java)
    }

    @Provides
    fun provideAirplanesService(retrofit: Retrofit): AirplanesService {
        return retrofit.create(AirplanesService::class.java)
    }

    @Provides
    fun provideRunwaysService(retrofit: Retrofit): RunwaysService {
        return retrofit.create(RunwaysService::class.java)
    }
}