package pl.kossa.myflights.hilt

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import pl.kossa.myflights.BuildConfig
import pl.kossa.myflights.api.call.ApiResponseAdapterFactory
import pl.kossa.myflights.api.services.*
import pl.kossa.myflights.repository.*
import pl.kossa.myflights.room.AppDatabase
import pl.kossa.myflights.utils.PreferencesHelper
import pl.kossa.myflights.utils.RetrofitDateSerializer
import pl.kossa.myflights.utils.analytics.AnalyticsTracker
import pl.kossa.myflights.utils.config.MyFlightsRemoteConfig
import pl.kossa.myflights.utils.fcm.FCMHandler
import pl.kossa.myflights.utils.language.UserLanguageManager
import pl.kossa.myflights.utils.links.DynamicLinksResolver
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun providePreferencesHelper(@ApplicationContext applicationContext: Context): PreferencesHelper {
        return PreferencesHelper(applicationContext)
    }

    @Singleton
    @Provides
    fun provideUserLanguageManager(): UserLanguageManager {
        return UserLanguageManager()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        preferencesHelper: PreferencesHelper,
        userLanguageManager: UserLanguageManager
    ): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor { chain ->
            val token = preferencesHelper.token
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .addHeader("Accept-Language", userLanguageManager.getCurrentLanguage())
                .build()
            chain.proceed(newRequest)
        }.build()
    }


    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().apply {
            registerTypeAdapter(Date::class.java, RetrofitDateSerializer())
        }.create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.SERVER_URL)
//            .baseUrl("http://192.168.200.139:8080/")
            .addCallAdapterFactory(ApiResponseAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Singleton
    @Provides
    fun provideFlightsService(retrofit: Retrofit): FlightsService {
        return retrofit.create(FlightsService::class.java)
    }

    @Singleton
    @Provides
    fun provideAirportsService(retrofit: Retrofit): AirportsService {
        return retrofit.create(AirportsService::class.java)
    }

    @Singleton
    @Provides
    fun provideAirplanesService(retrofit: Retrofit): AirplanesService {
        return retrofit.create(AirplanesService::class.java)
    }

    @Singleton
    @Provides
    fun provideRunwaysService(retrofit: Retrofit): RunwaysService {
        return retrofit.create(RunwaysService::class.java)
    }

    @Singleton
    @Provides
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Singleton
    @Provides
    fun provideImagesService(retrofit: Retrofit): ImagesService {
        return retrofit.create(ImagesService::class.java)
    }

    @Singleton
    @Provides
    fun provideStatisticsService(retrofit: Retrofit): StatisticsService {
        return retrofit.create(StatisticsService::class.java)
    }

    @Singleton
    @Provides
    fun provideSharedFlightsService(retrofit: Retrofit): SharedFlightsService {
        return retrofit.create(SharedFlightsService::class.java)
    }

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext applicationContext: Context): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "my-flights-db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideAirplaneRepository(
        airplanesService: AirplanesService,
        db: AppDatabase,
        preferencesHelper: PreferencesHelper
    ): AirplaneRepository {
        return AirplaneRepository(airplanesService, db.getAirplaneDao(), preferencesHelper)
    }

    @Singleton
    @Provides
    fun provideAirportRepository(
        airportsService: AirportsService,
        db: AppDatabase,
        preferencesHelper: PreferencesHelper
    ): AirportRepository {
        return AirportRepository(airportsService, db.getAirportDao(), preferencesHelper)
    }

    @Singleton
    @Provides
    fun provideRunwayRepository(
        runwaysService: RunwaysService,
        db: AppDatabase,
        preferencesHelper: PreferencesHelper
    ): RunwayRepository {
        return RunwayRepository(runwaysService, db.getRunwayDao(), preferencesHelper)
    }

    @Singleton
    @Provides
    fun provideFlightRepository(
        flightsService: FlightsService,
        db: AppDatabase,
        preferencesHelper: PreferencesHelper
    ): FlightRepository {
        return FlightRepository(flightsService, db.getFlightDao(), preferencesHelper)
    }

    @Singleton
    @Provides
    fun provideSharedFlightsRepository(
        sharedFlightsService: SharedFlightsService,
        preferencesHelper: PreferencesHelper
    ): SharedFlightRepository {
        return SharedFlightRepository(sharedFlightsService, preferencesHelper)
    }

    @Singleton
    @Provides
    fun provideUserRepository(
        userService: UserService,
        preferencesHelper: PreferencesHelper
    ): UserRepository {
        return UserRepository(userService, preferencesHelper)
    }

    @Singleton
    @Provides
    fun provideStatisticsRepository(
        statisticsService: StatisticsService,
        db: AppDatabase,
        preferencesHelper: PreferencesHelper
    ): StatisticsRepository {
        return StatisticsRepository(statisticsService, db.getStatisticsDao(), preferencesHelper)
    }

    @Singleton
    @Provides
    fun provideImageRepository(
        imagesService: ImagesService,
        preferencesHelper: PreferencesHelper
    ): ImageRepository {
        return ImageRepository(imagesService, preferencesHelper)
    }

    @Singleton
    @Provides
    fun provideAnalyticsTracker(): AnalyticsTracker {
        return AnalyticsTracker()
    }

    @Singleton
    @Provides
    fun provideFCMHandler(): FCMHandler {
        return FCMHandler()
    }

    @Singleton
    @Provides
    fun provideRemoteConfig(): MyFlightsRemoteConfig {
        return MyFlightsRemoteConfig()
    }

    @Singleton
    @Provides
    fun provideDynamicLinksResolver(
        @ApplicationContext applicationContext: Context
    ): DynamicLinksResolver {
        return DynamicLinksResolver(applicationContext)
    }
}