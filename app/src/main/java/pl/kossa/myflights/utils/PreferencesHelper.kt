package pl.kossa.myflights.utils

import android.content.Context
import com.google.firebase.auth.FirebaseAuth

class PreferencesHelper(applicationContext: Context) {

    companion object {
        internal const val PREFERENCES = "pl.kossa.myflights.prefs"
        internal const val TOKEN = "TOKEN"
        private const val FCM_TOKEN = "FCM_TOKEN"
        private const val LAST_JOIN_REQUEST_NOTIFICATION_ID = "LAST_JOIN_REQUEST_NOTIFICATION_ID"
        private const val MAX_NOTIFICATIONS = 5
    }

    private val preferences =
        applicationContext.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

    val userId
        get() = FirebaseAuth.getInstance().currentUser?.uid

    var token: String? = null
        get() {
            return preferences.getString(TOKEN, null)
        }
        set(value) {
            field = value
            preferences.edit().putString(TOKEN, value).apply()
        }

    var fcmToken: String? = null
        get() {
            return preferences.getString(FCM_TOKEN, null)
        }
        set(value) {
            field = value
            preferences.edit().putString(FCM_TOKEN, null).apply()
        }

    val nextJoinRequestNotificationId: Int
        get() {
            val lastValue = preferences.getInt(LAST_JOIN_REQUEST_NOTIFICATION_ID, 0)
            val nextValue = (lastValue + 1) % MAX_NOTIFICATIONS
            preferences.edit().putInt(LAST_JOIN_REQUEST_NOTIFICATION_ID, nextValue).apply()
            return nextValue
        }
}