package pl.kossa.myflights.utils.analytics

import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class AnalyticsTracker {

    private val analytics = Firebase.analytics

    fun setUserId(userId: String?) {
        analytics.setUserId(userId)
    }

    //TODO make list of events


    private enum class AnalyticsEvent(val eventName: String) {
        TEST("click")
    }

    private enum class AnalyticsEventParam(val paramName: String) {
        TEST("test")
    }
}