package pl.kossa.myflights.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import pl.kossa.myflights.R
import pl.kossa.myflights.api.requests.FcmRequest
import pl.kossa.myflights.api.services.UserService
import pl.kossa.myflights.utils.PreferencesHelper
import javax.inject.Inject

@AndroidEntryPoint
class MyFlightsFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    protected lateinit var userService: UserService

    @Inject
    protected lateinit var preferencesHelper: PreferencesHelper

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val notification = remoteMessage.notification
        notification?.let {
            showNotification(notification.title ?: "", notification.body ?: "")
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        preferencesHelper.fcmToken = token
        MainScope().launch {
            userService.putFcmToken(FcmRequest(token))
        }
    }

    private fun showNotification(title: String, message: String) {
        val channelId = getString(R.string.firebase_messaging_channel_id)
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setSound(sound)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelId, importance)
            val notificationManager =
                ContextCompat.getSystemService(this, NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }

        with(NotificationManagerCompat.from(this)) {
            notify(1, builder.build())
        }
    }
}

