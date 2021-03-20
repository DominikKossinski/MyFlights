package pl.kossa.myflights.hilt

import android.content.Context
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.android.synthetic.main.activity_main.view.*
import pl.kossa.myflights.R
import pl.kossa.myflights.activities.LoginActivity
import pl.kossa.myflights.activities.main.MainActivity
import pl.kossa.myflights.api.ApiService
import pl.kossa.myflights.application.MyFlightsApp
import pl.kossa.myflights.fragments.airports.AirportsViewModel
import pl.kossa.myflights.utils.PreferencesHelper

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun preferencesHelper(@ApplicationContext applicationContext: Context): PreferencesHelper {
        return PreferencesHelper(applicationContext)
    }

    @Provides
    fun provideApiService(preferencesHelper: PreferencesHelper): ApiService {
        return ApiService(preferencesHelper)
    }
    
}