package pl.kossa.myflights.utils.analytics

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class AnalyticsTracker {

    private val analytics = Firebase.analytics

    fun setUserId(userId: String?) {
        analytics.setUserId(userId)
    }


    fun logClickShareFlight() {
        logEvent(AnalyticsEvent.CLICK_SHARE_FLIGHT)
    }

    fun logClickResignFromFlight() {
        logEvent(AnalyticsEvent.CLICK_RESIGN_FROM_FLIGHT)
    }

    fun logClickDeleteFlight() {
        logEvent(AnalyticsEvent.CLICK_DELETE_FLIGHT)
    }

    fun logClickGoToSharedUsers() {
        logEvent(AnalyticsEvent.CLICK_GO_TO_SHARED_USERS)
    }

    private fun logEvent(event: AnalyticsEvent, params: Bundle? = null) {
        Log.d("MyLog", "Event: $event Params: $params")
//        analytics.logEvent(event.eventName, params)
    }


    private enum class AnalyticsEvent(val eventName: String) {

        // FlightDetails
        CLICK_SHARE_FLIGHT("click_share_flight"),
        CLICK_RESIGN_FROM_FLIGHT("click_resign_from_flight"),
        CLICK_DELETE_FLIGHT("click_delete_flight"),
        CLICK_GO_TO_SHARED_USERS("click_go_to_shared_users")
    }

    private enum class AnalyticsEventParam(val paramName: String) {
        TEST("test")
    }
}