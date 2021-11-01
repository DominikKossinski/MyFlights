package pl.kossa.myflights.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.qualifiers.ApplicationContext

class PreferencesHelper(applicationContext: Context) {

    companion object {
        internal const val TOKEN = "TOKEN"
        internal const val PREFERENCES = "pl.kossa.myflights.prefs"
    }

    private val preferences =
        applicationContext.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

    var token: String? = null
        get() {
            return preferences.getString(TOKEN, null)
        }
        set(value) {
            field = value
            preferences.edit().putString(TOKEN, value).apply()
        }
}