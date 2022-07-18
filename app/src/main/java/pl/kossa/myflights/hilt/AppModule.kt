package pl.kossa.myflights.hilt

import android.content.Context
import android.util.Log
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
import pl.kossa.myflights.utils.fcm.FCMHandler
import pl.kossa.myflights.utils.language.UserLanguageManager
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun providePreferencesHelper(@ApplicationContext applicationContext: Context): PreferencesHelper {
        return PreferencesHelper(applicationContext)
    }

    @Provides
    fun provideUserLanguageManager(): UserLanguageManager {
        return UserLanguageManager()
    }

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


    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().apply {
            registerTypeAdapter(Date::class.java, RetrofitDateSerializer())
        }.create()
    }

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

    @Provides
    fun provideStatisticsService(retrofit: Retrofit): StatisticsService {
        return retrofit.create(StatisticsService::class.java)
    }

    @Provides
    fun provideSharedFlightsService(retrofit: Retrofit): SharedFlightsService {
        return retrofit.create(SharedFlightsService::class.java)
    }

    @Provides
    fun provideRoomDatabase(@ApplicationContext applicationContext: Context): AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "my-flights-db"
        ).build()
    }

    @Provides
    fun provideAirplaneRepository(
        airplanesService: AirplanesService,
        db: AppDatabase,
        preferencesHelper: PreferencesHelper
    ): AirplaneRepository {
        return AirplaneRepository(airplanesService, db.getAirplaneDao(), preferencesHelper)
    }

    @Provides
    fun provideAirportRepository(
        airportsService: AirportsService,
        db: AppDatabase,
        preferencesHelper: PreferencesHelper
    ): AirportRepository {
        return AirportRepository(airportsService, db.getAirportDao(), preferencesHelper)
    }

    @Provides
    fun provideRunwayRepository(
        runwaysService: RunwaysService,
        db: AppDatabase,
        preferencesHelper: PreferencesHelper
    ): RunwayRepository {
        return RunwayRepository(runwaysService, db.getRunwayDao(), preferencesHelper)
    }

    @Provides
    fun provideFlightRepository(
        flightsService: FlightsService,
        db: AppDatabase,
        preferencesHelper: PreferencesHelper
    ): FlightRepository {
        return FlightRepository(flightsService, db.getFlightDao(), preferencesHelper)
    }

    @Provides
    fun provideSharedFlightsRepository(
        sharedFlightsService: SharedFlightsService,
        preferencesHelper: PreferencesHelper
    ): SharedFlightRepository {
        return SharedFlightRepository(sharedFlightsService, preferencesHelper)
    }

    @Provides
    fun provideUserRepository(
        userService: UserService,
        preferencesHelper: PreferencesHelper
    ): UserRepository {
        return UserRepository(userService, preferencesHelper)
    }

    @Provides
    fun provideStatisticsRepository(
        statisticsService: StatisticsService,
        db: AppDatabase,
        preferencesHelper: PreferencesHelper
    ): StatisticsRepository {
        return StatisticsRepository(statisticsService, db.getStatisticsDao(), preferencesHelper)
    }

    @Provides
    fun provideImageRepository(
        imagesService: ImagesService,
        preferencesHelper: PreferencesHelper
    ): ImageRepository {
        return ImageRepository(imagesService, preferencesHelper)
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