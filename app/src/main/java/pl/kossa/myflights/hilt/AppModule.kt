package pl.kossa.myflights.hilt

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import org.simpleframework.xml.convert.Registry
import org.simpleframework.xml.convert.RegistryStrategy
import org.simpleframework.xml.core.Persister
import pl.kossa.myflights.BuildConfig
import pl.kossa.myflights.analytics.AnalyticsTracker
import pl.kossa.myflights.api.call.ApiResponseAdapterFactory
import pl.kossa.myflights.api.server.services.*
import pl.kossa.myflights.api.simbrief.models.Alternate
import pl.kossa.myflights.api.simbrief.models.AlternateConverter
import pl.kossa.myflights.api.simbrief.services.SimbriefService
import pl.kossa.myflights.fcm.FCMHandler
import pl.kossa.myflights.utils.PreferencesHelper
import pl.kossa.myflights.utils.RetrofitDateSerializer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.*
import javax.inject.Qualifier

@Qualifier
annotation class MyFlightServer
@Qualifier
annotation class Simbrief

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

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
//            Log.d("MyLog", "Token: ${preferencesHelper.token}")
//            Log.d("MyLog", "$newRequest")
            chain.proceed(newRequest)
        }.build()
    }

    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().apply {
            registerTypeAdapter(Date::class.java, RetrofitDateSerializer())
        }.create()
    }

    @Provides
    @MyFlightServer
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.SERVER_URL)
            .addCallAdapterFactory(ApiResponseAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun provideFlightsService(@MyFlightServer retrofit: Retrofit): FlightsService {
        return retrofit.create(FlightsService::class.java)
    }

    @Provides
    fun provideAirportsService(@MyFlightServer retrofit: Retrofit): AirportsService {
        return retrofit.create(AirportsService::class.java)
    }

    @Provides
    fun provideAirplanesService(@MyFlightServer retrofit: Retrofit): AirplanesService {
        return retrofit.create(AirplanesService::class.java)
    }

    @Provides
    fun provideRunwaysService(@MyFlightServer retrofit: Retrofit): RunwaysService {
        return retrofit.create(RunwaysService::class.java)
    }

    @Provides
    fun provideUserService(@MyFlightServer retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    fun provideImagesService(@MyFlightServer retrofit: Retrofit): ImagesService {
        return retrofit.create(ImagesService::class.java)
    }

    @Provides
    fun provideOFPsService(@MyFlightServer retrofit: Retrofit): OFPsService {
        return retrofit.create(OFPsService::class.java)
    }

    @Provides
    fun provideAnalyticsTracker(): AnalyticsTracker {
        return AnalyticsTracker()
    }

    @Provides
    fun provideFCMHandler(): FCMHandler {
        return FCMHandler()
    }

}

@InstallIn(SingletonComponent::class)
@Module
object SimbriefModule {

    @Provides
    @Simbrief
    fun provideRetrofit(): Retrofit {
        val registry = Registry()
        val strategy  = RegistryStrategy(registry)
        val serializer = Persister(strategy)
        registry.bind(Alternate::class.java, AlternateConverter::class.java)
        return Retrofit.Builder()
            .baseUrl(BuildConfig.SIMBRIEF_URL)
            .addCallAdapterFactory(ApiResponseAdapterFactory())
            .addConverterFactory(SimpleXmlConverterFactory.create(serializer))
            .build()
    }

    @Provides
    fun provideSimbriefService(@Simbrief retrofit: Retrofit): SimbriefService {
        return retrofit.create(SimbriefService::class.java)
    }
}
