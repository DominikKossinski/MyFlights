package pl.kossa.myflights.utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

class PreferencesHelper(activity: AppCompatActivity) {

    companion object {
        private const val TOKEN = "TOKEN"
        private const val PREFERENCES = "pl.kossa.myflights.prefs"
    }

    private val preferences = activity.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

    var token: String? = null
        get() {
            return preferences.getString(TOKEN, null)
        }
        set(value) {
            field = value
            preferences.edit().putString(TOKEN, value).apply()
        }
}