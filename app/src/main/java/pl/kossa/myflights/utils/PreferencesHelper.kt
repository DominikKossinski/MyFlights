package pl.kossa.myflights.utils

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import pl.kossa.myflights.api.models.User

class PreferencesHelper(applicationContext: Context) {

    companion object {
        internal const val PREFERENCES = "pl.kossa.myflights.prefs"
        internal const val TOKEN = "TOKEN"
        private const val FCM_TOKEN = "FCM_TOKEN"
        private const val LAST_JOIN_REQUEST_NOTIFICATION_ID = "LAST_JOIN_REQUEST_NOTIFICATION_ID"
        private const val LAST_ACCEPTED_JOIN_REQUEST_NOTIFICATION_ID =
            "LAST_ACCEPTED_JOIN_REQUEST_NOTIFICATION_ID"
        private const val MAX_NOTIFICATIONS = 5

        private const val USER_NICK = "USER_NICK"
        private const val USER_EMAIL = "USER_EMAIL"
        private const val USER_AVATAR_URL = "USER_AVATAR_URL"
    }

    private val preferences =
        applicationContext.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)

    val userId
        get() = FirebaseAuth.getInstance().currentUser?.uid

    var userNick: String? = null
        get() {
            return preferences.getString(USER_NICK, null)
        }
        private set(value) {
            field = value
            preferences.edit().putString(USER_NICK, value).apply()
        }

    var userEmail: String? = null
        get() {
            return preferences.getString(USER_EMAIL, null)
        }
        private set(value) {
            field = value
            return preferences.edit().putString(USER_EMAIL, value).apply()
        }

    var avatarUrl: String? = null
        get() {
            return preferences.getString(USER_AVATAR_URL, null)
        }
        private set(value) {
            field = value
            return preferences.edit().putString(USER_AVATAR_URL, value).apply()
        }

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

    val nextAcceptedJoinRequestNotificationId: Int
        get() {
            val lastValue = preferences.getInt(LAST_ACCEPTED_JOIN_REQUEST_NOTIFICATION_ID, 0)
            val nextValue = (lastValue + 1) % MAX_NOTIFICATIONS
            preferences.edit().putInt(LAST_ACCEPTED_JOIN_REQUEST_NOTIFICATION_ID, nextValue).apply()
            return nextValue
        }


    fun setUser(user: User?) {
        userNick = user?.nick
        userEmail = user?.email
        avatarUrl = user?.avatar?.url
    }
}