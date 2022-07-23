package pl.kossa.myflights.utils.config

import android.util.Log
import com.google.firebase.crashlytics.internal.common.CrashlyticsCore
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.mapLatest
import pl.kossa.myflights.R

@OptIn(ExperimentalCoroutinesApi::class)
class MyFlightsRemoteConfig {

    private val remoteConfig = Firebase.remoteConfig

    init {
        val settings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3_600
        }
        remoteConfig.setConfigSettingsAsync(settings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.fetchAndActivate().addOnSuccessListener {
            refresh.tryEmit(Unit)
        }.addOnFailureListener {
            Firebase.crashlytics.recordException(it)
        }
    }

    private val refresh = MutableSharedFlow<Unit>(replay = 0, extraBufferCapacity = 1)

    fun getButtonText(): Flow<String> = refresh.mapLatest {
        remoteConfig.getString(
            TEST_BUTTON_TEXT
        )
    }

    companion object {
        private const val TEST_BUTTON_TEXT = "TEST_BUTTON_TEXT"
    }
}