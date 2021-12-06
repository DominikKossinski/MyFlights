package pl.kossa.myflights.fcm

import com.google.firebase.messaging.FirebaseMessaging

class FCMHandler {

    fun enableFCM() {
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
    }

    fun disableFCM(onSuccess: () -> Unit) {
        FirebaseMessaging.getInstance().isAutoInitEnabled = false
        FirebaseMessaging.getInstance().deleteToken().addOnSuccessListener {
            onSuccess.invoke()
        }
    }
}