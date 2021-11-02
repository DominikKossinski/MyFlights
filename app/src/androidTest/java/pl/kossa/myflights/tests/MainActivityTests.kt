package pl.kossa.myflights.tests

import android.util.Log
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.google.gson.GsonBuilder
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pl.kossa.myflights.activities.main.MainActivity
import pl.kossa.myflights.api.models.Airplane
import pl.kossa.myflights.hilt.AppModule
import pl.kossa.myflights.server.MyFlightsMockServer

@RunWith(AndroidJUnit4::class)
@LargeTest
@UninstallModules(AppModule::class)
@HiltAndroidTest
class MainActivityTests {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val server  = MyFlightsMockServer().apply {
        start(8080)
    }

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @After
    fun clear() {
        server.shutdown()
    }

    @Test
    fun abc() {
        Log.d("MyLog", "Abc test")
        Thread.sleep(1_000)
    }
}