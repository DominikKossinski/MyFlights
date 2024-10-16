package pl.kossa.myflights.hilt

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.OkHttpClient
import pl.kossa.myflights.api.call.ApiResponseAdapterFactory
import pl.kossa.myflights.api.services.*
import pl.kossa.myflights.utils.PreferencesHelper
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [AppModule::class]
)
object TestAppModule {

    @Provides
    fun providePreferencesHelper(@ApplicationContext applicationContext: Context): PreferencesHelper {
        return PreferencesHelper(applicationContext)
    }

    @Provides
    fun provideOkHttpClient(
        preferencesHelper: PreferencesHelper
    ): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${preferencesHelper.token}")
                .build()
            Log.d("MyLog", "Token: ${preferencesHelper.token}")
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
//            .baseUrl("http://10.0.2.2:8080")
            .baseUrl("http://127.0.0.1:8080")
            .addCallAdapterFactory(ApiResponseAdapterFactory())
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

    @Provides
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    fun provideImagesService(retrofit: Retrofit): ImagesService {
        return retrofit.create(ImagesService::class.java)
    }

}