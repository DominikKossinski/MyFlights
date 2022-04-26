package pl.kossa.myflights.utils.fcm

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

class FCMHandler {

    fun enableFCM(onSuccess: () -> Unit) {
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        FirebaseMessaging.getInstance().subscribeToTopic("all")
        onSuccess.invoke()
        //TODO handle error
    }

    fun disableFCM(onSuccess: () -> Unit) {
        FirebaseMessaging.getInstance().isAutoInitEnabled = false
        FirebaseMessaging.getInstance().deleteToken().addOnSuccessListener {
            FirebaseMessaging.getInstance().unsubscribeFromTopic("all").addOnSuccessListener {
                onSuccess.invoke()
            }
        }
    }

    fun refreshFCMToken() {
        FirebaseMessaging.getInstance().deleteToken().addOnSuccessListener {
            FirebaseMessaging.getInstance().token
        }.addOnFailureListener {
            Log.d("MyLog", "Exception: $it")
        }
    }
}