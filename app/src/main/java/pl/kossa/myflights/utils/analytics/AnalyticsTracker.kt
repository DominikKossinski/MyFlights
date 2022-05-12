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

    fun logJoinRequestScanned() {
        logEvent(AnalyticsEvent.JOIN_REQUEST_SCANNED)
    }

    fun logClickJoinFlight() {
        logEvent(AnalyticsEvent.CLICK_JOIN_FLIGHT)
    }

    fun logClickShareWithLink() {
        logEvent(AnalyticsEvent.CLICK_SHARE_WITH_LINK)
    }

    fun logClickLogin() {
        logEvent(AnalyticsEvent.CLICK_LOGIN)
    }

    fun logClickGoogleSignIn() {
        logEvent(AnalyticsEvent.CLICK_GOOGLE_SIGN_IN)
    }

    fun logClickCreateAccount() {
        logEvent(AnalyticsEvent.CLICK_CREATE_ACCOUNT)
    }

    fun logClickStatistics() {
        logEvent(AnalyticsEvent.CLICK_STATISTICS)
    }

    fun logClickSettings() {
        logEvent(AnalyticsEvent.CLICK_SETTINGS)
    }

    fun logClickSignOut() {
        logEvent(AnalyticsEvent.CLICK_SIGN_OUT)
    }

    fun logClickScanQRCode() {
        logEvent(AnalyticsEvent.CLICK_SCAN_QR_CODE)
    }

    fun logClickFlightDetails() {
        logEvent(AnalyticsEvent.CLICK_FLIGHT_DETAILS)
    }

    fun logClickAddFlight() {
        logEvent(AnalyticsEvent.CLICK_ADD_FLIGHT)
    }

    fun logClickAirportDetails() {
        logEvent(AnalyticsEvent.CLICK_AIRPORT_DETAILS)
    }

    fun logClickDeleteAirport() {
        logEvent(AnalyticsEvent.CLICK_DELETE_AIRPORT)
    }

    fun logClickAddAirport() {
        logEvent(AnalyticsEvent.CLICK_ADD_AIRPORT)
    }

    fun logClickRunwayDetails() {
        logEvent(AnalyticsEvent.CLICK_RUNWAY_DETAILS)
    }

    fun logClickAddRunway() {
        logEvent(AnalyticsEvent.CLICK_ADD_RUNWAY)
    }

    fun logClickDeleteRunway() {
        logEvent(AnalyticsEvent.CLICK_DELETE_RUNWAY)
    }

    fun logClickAboutApp() {
        logEvent(AnalyticsEvent.CLICK_ABOUT_APP)
    }

    fun logClickAirplaneDetails() {
        logEvent(AnalyticsEvent.CLICK_AIRPLANE_DETAILS)
    }

    fun logClickAddAirplane() {
        logEvent(AnalyticsEvent.CLICK_ADD_AIRPLANE)
    }

    fun logClickDeleteAirplane() {
        logEvent(AnalyticsEvent.CLICK_DELETE_AIRPLANE)
    }

    fun logClickDeleteAccount() {
        logEvent(AnalyticsEvent.CLICK_DELETE_ACCOUNT)
    }

    private fun logEvent(event: AnalyticsEvent, params: Bundle? = null) {
        Log.d("MyLog", "Event: $event Params: $params")
//        analytics.logEvent(event.eventName, params)
    }

    private enum class AnalyticsEvent(val eventName: String) {
        // LoginFragment
        CLICK_LOGIN("click_login"),
        CLICK_GOOGLE_SIGN_IN("click_google_sign_in"),

        // CreateAccount
        CLICK_CREATE_ACCOUNT("click_create_account"),

        // FlightsFragment
        CLICK_FLIGHT_DETAILS("click_flight_details"),

        // AirplanesFragment
        CLICK_AIRPLANE_DETAILS("click_airplane_details"),

        // AirplaneDetails
        CLICK_DELETE_AIRPLANE("click_delete_airplane"),

        // FlightDetailsFragment
        CLICK_SHARE_FLIGHT("click_share_flight"),
        CLICK_RESIGN_FROM_FLIGHT("click_resign_from_flight"),
        CLICK_DELETE_FLIGHT("click_delete_flight"),
        CLICK_GO_TO_SHARED_USERS("click_go_to_shared_users"),

        // JoinFlightBottomSheet
        JOIN_REQUEST_SCANNED("join_request_scanned"),
        CLICK_JOIN_FLIGHT("click_join_flight"),

        //ShareFlightBottomSheet
        CLICK_SHARE_WITH_LINK("click_share_with_link"),

        // FlightAdd
        CLICK_ADD_FLIGHT("click_add_flight"),
        CLICK_SCAN_QR_CODE("click_scan_qr_code"),

        // AirplaneAdd
        CLICK_ADD_AIRPLANE("click_add_airplane"),

        // AirportsFragment
        CLICK_AIRPORT_DETAILS("click_airport_details"),

        // AirportDetails
        CLICK_DELETE_AIRPORT("click_delete_airport"),
        CLICK_RUNWAY_DETAILS("click_runway_details"),

        // AirportAdd
        CLICK_ADD_AIRPORT("click_add_airport"),

        // RunwayDetails
        CLICK_DELETE_RUNWAY("click_delete_runway"),

        // RunwayAdd
        CLICK_ADD_RUNWAY("click_add_runway"),

        // Profile
        CLICK_STATISTICS("click_statistics"),
        CLICK_SETTINGS("click_settings"),
        CLICK_SIGN_OUT("click_sign_out"),

        // Settings
        CLICK_ABOUT_APP("click_about_app"),

        // AccountDelete
        CLICK_DELETE_ACCOUNT("click_account_delete")

    }

    private enum class AnalyticsEventParam(val paramName: String) {
        TEST("test")
    }
}