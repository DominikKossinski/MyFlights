package pl.kossa.myflights.utils

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import pl.kossa.myflights.api.models.Image
import pl.kossa.myflights.api.models.ProviderType
import pl.kossa.myflights.api.models.User

class PreferencesHelper(applicationContext: Context) {

    companion object {
        internal const val PREFERENCES = "pl.kossa.myflights.prefs"
        internal const val TOKEN = "TOKEN"
        private const val FCM_TOKEN = "FCM_TOKEN"
        private const val NICK = "NICK"
        private const val EMAIL = "EMAIL"
        private const val AVATAR_ID = "AVATAR_ID"
        private const val AVATAR_URL = "AVATAR_URL"
        private const val AVATAR_THUMBNAIL_URL = "AVATAR_THUMBNAIL_URL"
        private const val PROVIDER_TYPE = "PROVIDER_TYPE"
        private const val REGULATIONS_ACCEPTED = "REGULATIONS_ACCEPTED"
        private const val LAST_JOIN_REQUEST_NOTIFICATION_ID = "LAST_JOIN_REQUEST_NOTIFICATION_ID"
        private const val LAST_ACCEPTED_JOIN_REQUEST_NOTIFICATION_ID =
            "LAST_ACCEPTED_JOIN_REQUEST_NOTIFICATION_ID"
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

    var nick: String? = null
        get() {
            return preferences.getString(NICK, null)
        }
        set(value) {
            field = value
            preferences.edit().putString(NICK, value).apply()
        }

    var email: String? = null
        get() {
            return preferences.getString(EMAIL, null)
        }
        set(value) {
            field = value
            preferences.edit().putString(EMAIL, value).apply()
        }

    var fcmToken: String? = null
        get() {
            return preferences.getString(FCM_TOKEN, null)
        }
        set(value) {
            field = value
            preferences.edit().putString(FCM_TOKEN, null).apply()
        }

    var avatar: Image? = null
        get() {
            val avatarId = preferences.getString(AVATAR_ID, null)
            val avatarUrl = preferences.getString(AVATAR_URL, null)
            val avatarThumbnailUrl = preferences.getString(AVATAR_THUMBNAIL_URL, null)
            if (avatarId != null && avatarUrl != null && avatarThumbnailUrl != null) {
                return Image(avatarId, avatarUrl, avatarThumbnailUrl)
            }
            return null
        }
        set(value) {
            field = value
            preferences.edit()
                .putString(AVATAR_ID, value?.imageId)
                .putString(AVATAR_URL, value?.url)
                .putString(AVATAR_THUMBNAIL_URL, value?.thumbnailUrl)
                .apply()
        }

    var providerType: ProviderType? = null
        get() {
            return preferences.getString(PROVIDER_TYPE, null)?.let {
                ProviderType.valueOf(it)
            }
        }
        set(value) {
            field = value
            preferences.edit().putString(PROVIDER_TYPE, value.toString()).apply()
        }

    var regulationsAccepted: Boolean = false
        get() {
            return preferences.getBoolean(REGULATIONS_ACCEPTED, false)
        }
        set(value) {
            field = value
            preferences.edit().putBoolean(REGULATIONS_ACCEPTED, value)
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


    fun setUserData(user: User) {
        nick = user.nick
        avatar = user.avatar
        email = user.email
        providerType = user.providerType
        regulationsAccepted = user.regulationsAccepted
    }

    fun getUser(): User? {
        val uId = userId
        val uEmail = email
        val uNick = nick
        val uProviderType = providerType
        return if(uId != null && uNick != null && uEmail != null && uProviderType != null) {
            User(
                uId,
                uNick,
                uEmail,
                avatar,
                regulationsAccepted,
                uProviderType
            )
        } else null
    }

    fun clearUserData() {
        nick = null
        avatar = null
        email = null
        providerType = null
        regulationsAccepted = false
        token = null
        fcmToken = null
    }
}