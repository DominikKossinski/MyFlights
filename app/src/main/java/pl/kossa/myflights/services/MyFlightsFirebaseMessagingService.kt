package pl.kossa.myflights.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import pl.kossa.myflights.R
import pl.kossa.myflights.activities.main.MainActivity
import pl.kossa.myflights.api.requests.FcmRequest
import pl.kossa.myflights.api.services.UserService
import pl.kossa.myflights.utils.PreferencesHelper
import pl.kossa.myflights.utils.fcm.NotificationType
import javax.inject.Inject

@AndroidEntryPoint
class MyFlightsFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var userService: UserService

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val data = remoteMessage.data
        val deepLink = data["deepLink"]
        val title = data["title"] ?: ""
        val body = data["body"] ?: ""
        val notificationType = data["notificationType"]?.let {
            NotificationType.values().find { type -> type.name == it }
        }
        when (notificationType) {
            NotificationType.USER_ACCEPTED_JOIN_REQUEST -> {
                showAcceptedJoinRequestNotification(title, body, data)
            }
            NotificationType.USER_SEND_JOIN_REQUEST -> {
                showJoinRequestNotification(title, body, data)
            }
            else -> {
                Log.d("MyLog", "DeepLink: $deepLink")
                showNotification(title, body, deepLink)
            }
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        preferencesHelper.fcmToken = token
        MainScope().launch {
            try {
                Log.d("MyLog", "New FCM token: $token")
                userService.putFcmToken(FcmRequest(token))
            } catch (e: Exception) {

            }
        }
    }

    private fun showJoinRequestNotification(
        title: String,
        body: String,
        data: Map<String, String>
    ) {
        val sharedFlightId = data["sharedFlightId"]
        val pendingIntent = sharedFlightId?.let {
            NavDeepLinkBuilder(applicationContext)
                .setGraph(R.navigation.main_nav_graph)
                .setDestination(R.id.pendingSharedFlightsDetailsFragment)
                .setComponentName(MainActivity::class.java)
                .setArguments(Bundle().apply {
                    putString("sharedFlightId", sharedFlightId)
                })
                .createPendingIntent()
        }
        val channelId = getString(R.string.firebase_messaging_channel_id)
        val notification = createNotification(
            channelId, title, body, pendingIntent
        )
        notify(preferencesHelper.nextJoinRequestNotificationId, channelId, notification)
    }

    private fun showAcceptedJoinRequestNotification(
        title: String,
        body: String,
        data: Map<String, String>
    ) {
        val flightId = data["flightId"]
        val pendingIntent = flightId?.let {
            NavDeepLinkBuilder(applicationContext)
                .setGraph(R.navigation.main_nav_graph)
                .setDestination(R.id.flightDetailsFragment)
                .setComponentName(MainActivity::class.java)
                .setArguments(Bundle().apply {
                    putString("flightId", flightId)
                })
                .createPendingIntent()
        }
        val channelId = getString(R.string.firebase_messaging_channel_id)
        val notification = createNotification(
            channelId, title, body, pendingIntent
        )
        notify(preferencesHelper.nextAcceptedJoinRequestNotificationId, channelId, notification)
    }

    private fun showNotification(title: String, message: String, deepLink: String?) {
        val channelId = getString(R.string.firebase_messaging_channel_id)
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val pendingIntent = deepLink?.let { link ->
            PendingIntent.getActivity(this, 0, Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(link)
            }, PendingIntent.FLAG_ONE_SHOT)
        }
        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setSound(sound)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        pendingIntent?.let { builder.setContentIntent(pendingIntent) }
        notify(1, channelId, builder.build())
    }

    private fun notify(id: Int, channelId: String, notification: Notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelId, importance)
            val notificationManager =
                ContextCompat.getSystemService(this, NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }

        with(NotificationManagerCompat.from(this)) {
            notify(id, notification)
        }
    }

    private fun createNotification(
        channelId: String,
        title: String,
        body: String,
        pendingIntent: PendingIntent?
    ): Notification {
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setSound(sound)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        pendingIntent?.let { builder.setContentIntent(pendingIntent) }
        return builder.build()
    }
}

