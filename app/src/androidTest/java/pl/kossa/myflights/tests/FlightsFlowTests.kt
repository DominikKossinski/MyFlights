package pl.kossa.myflights.tests

import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.android.awaitFrame
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pl.kossa.myflights.R
import pl.kossa.myflights.activities.main.MainActivity
import pl.kossa.myflights.hilt.AppModule
import pl.kossa.myflights.server.MyFlightsMockServer
import pl.kossa.myflights.utils.AlertDialogButton
import java.lang.IllegalStateException
import java.lang.Thread.sleep

@RunWith(AndroidJUnit4::class)
@LargeTest
@UninstallModules(AppModule::class)
@HiltAndroidTest
class FlightsFlowTests {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    private val server = MyFlightsMockServer().apply {
        start(8080)
    }

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @After
    fun clear() {
        server.shutdown()
    }

    @Test
    fun openFlightDetails() {
        onView(withId(R.id.flightsRecyclerView))
            .perform(
                actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )
    }

    private fun openAddFlight() {
        onView(withId(R.id.fab))
            .perform(click())
    }

    private fun selectAirplane() {
        onView(allOf(withId(R.id.elementSelectButton), withParent(withId(R.id.airplaneSelectView))))
            .perform(click())
        onView(withId(R.id.airplanesRecyclerView)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )
    }

    private fun selectDepartureAirport() {
        onView(withId(R.id.departureRunwaySelectView)).check(
            matches(
                withEffectiveVisibility(
                    Visibility.GONE
                )
            )
        )
        onView(
            allOf(
                withId(R.id.elementSelectButton),
                withParent(withId(R.id.departureAirportSelectView))
            )
        )
            .perform(click())
        onView(withId(R.id.airportsRecyclerView)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )
    }

    private fun selectDepartureRunway() {
        onView(withId(R.id.departureRunwaySelectView)).check(
            matches(
                withEffectiveVisibility(
                    Visibility.VISIBLE
                )
            )
        )
        onView(
            allOf(
                withId(R.id.elementSelectButton),
                withParent(withId(R.id.departureRunwaySelectView))
            )
        )
            .perform(click())
        onView(withId(R.id.runwaysRecyclerView)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )
    }


    private fun selectArrivalAirport() {
        onView(withId(R.id.arrivalRunwaySelectView)).check(
            matches(
                withEffectiveVisibility(
                    Visibility.GONE
                )
            )
        )
        onView(
            allOf(
                withId(R.id.elementSelectButton),
                withParent(withId(R.id.arrivalAirportSelectView))
            )
        )
            .perform(click())


        onView(withId(R.id.airportsRecyclerView)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )
    }

    private fun waitForId(@IdRes id: Int): Matcher<View>? {
        sleep(10)
        return withId(id)
    }

    private fun selectArrivalRunway() {
        onView(withId(R.id.arrivalRunwaySelectView)).check(
            matches(
                withEffectiveVisibility(
                    Visibility.VISIBLE
                )
            )
        )
        onView(
            allOf(
                withId(R.id.elementSelectButton),
                withParent(withId(R.id.arrivalRunwaySelectView))
            )
        )
            .perform(click())


        onView(waitForId(R.id.runwaysRecyclerView))
            .perform(swipeUp())
        onView(withId(R.id.runwaysRecyclerView)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click())
        )
    }

    private fun selectDepartureDate() {
        onView(allOf(withId(R.id.dateTimeSelectButton), withParent(withId(R.id.departureDts))))
            .perform(click())
        onView(withClassName(equalTo(DatePicker::class.java.name)))
            .perform(PickerActions.setDate(2021, 10, 5))
        onView(withId(AlertDialogButton.POSITIVE.resId))
            .perform(click())

        onView(withClassName(equalTo(TimePicker::class.java.name)))
            .perform(PickerActions.setTime(15, 30))
        onView(withId(AlertDialogButton.POSITIVE.resId))
            .perform(click())
    }

    private fun selectArrivalDate() {
        onView(allOf(withId(R.id.dateTimeSelectButton), withParent(withId(R.id.arrivalDts))))
            .perform(click())
        onView(withClassName(equalTo(DatePicker::class.java.name)))
            .perform(PickerActions.setDate(2021, 10, 5))
        onView(withId(AlertDialogButton.POSITIVE.resId))
            .perform(click())

        onView(withClassName(equalTo(TimePicker::class.java.name)))
            .perform(PickerActions.setTime(17, 30))
        onView(withId(AlertDialogButton.POSITIVE.resId))
            .perform(click())
    }


    private fun addFlight() {
        openAddFlight()
        onView(withId(R.id.addButton))
            .check(matches(isNotEnabled()))
        onView(withId(R.id.saveIv))
            .check(matches(isNotEnabled()))

        selectAirplane()
        selectDepartureAirport()
        selectDepartureRunway()
        selectArrivalAirport()
        onView(withId(R.id.nestedScrollView)).perform(swipeUp())
        selectArrivalRunway()
        selectDepartureDate()
        selectArrivalDate()

        onView(withId(R.id.addButton))
            .check(matches(isEnabled()))
        onView(withId(R.id.saveIv))
            .check(matches(isEnabled()))

        onView(withId(R.id.distanceTie))
            .perform(typeText("123"), pressImeActionButton())
        onView(withId(R.id.noteTie))
            .perform(typeText("Note from flight"), closeSoftKeyboard())

        onView(withId(R.id.addButton))
            .perform(click())


        onView(allOf(withId(R.id.backTv), withParent(withId(R.id.backAppbar)))).perform(click())
    }

    private fun openSecondFlightDetails() {
        addFlight()

        onView(withId(R.id.flightsRecyclerView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))

        onView(allOf(withId(R.id.valueTextView), withParent(withId(R.id.airplaneEwtv))))
            .check(matches(withText("A380")))
        onView(allOf(withId(R.id.valueTextView), withParent(withId(R.id.departureAirportEwtv))))
            .check(matches(withText("Okęcie (EPWA)")))
        onView(allOf(withId(R.id.valueTextView), withParent(withId(R.id.departureRunwayEwtv))))
            .check(matches(withText("11")))

        onView(allOf(withId(R.id.valueTextView), withParent(withId(R.id.arrivalAirportEwtv))))
            .check(matches(withText("Okęcie (EPWA)")))
        onView(allOf(withId(R.id.valueTextView), withParent(withId(R.id.arrivalRunwayEwtv))))
            .check(matches(withText("15")))

        onView(allOf(withId(R.id.valueTextView), withParent(withId(R.id.departureTimeEwtv))))
            .check(matches(withText("05.10.2021 15:30")))
        onView(allOf(withId(R.id.valueTextView), withParent(withId(R.id.arrivalTimeEwtv))))
            .check(matches(withText("05.10.2021 17:30")))

        onView(allOf(withId(R.id.valueTextView), withParent(withId(R.id.distanceEwtv))))
            .check(matches(withText("123")))
        onView(withId(R.id.noteTv))
            .check(matches(withText("Note from flight")))

    }

    private fun changeDepartureRunway() {

        onView(
            allOf(
                withId(R.id.elementChangeButton),
                withParent(withId(R.id.departureRunwaySelectView))
            )
        )
            .perform(click())
        onView(withId(R.id.runwaysRecyclerView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))
    }

    private fun changeArrivalRunway() {
        onView(withId(R.id.nestedScrollView))
            .perform(swipeUp())
        onView(
            allOf(
                withId(R.id.elementChangeButton),
                withParent(withId(R.id.arrivalRunwaySelectView))
            )
        )
            .perform(click())

        onView(waitForId(R.id.runwaysRecyclerView))
            .perform(swipeUp())
        onView(withId(R.id.runwaysRecyclerView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
    }

    private fun changeDepartureDate() {
        onView(allOf(withId(R.id.dateTimeChangeButton), withParent(withId(R.id.departureDts))))
            .perform(click())
        onView(withClassName(equalTo(DatePicker::class.java.name)))
            .perform(PickerActions.setDate(2021, 5, 6))
        onView(withId(AlertDialogButton.POSITIVE.resId))
            .perform(click())

        onView(withClassName(equalTo(TimePicker::class.java.name)))
            .perform(PickerActions.setTime(11, 29))
        onView(withId(AlertDialogButton.POSITIVE.resId))
            .perform(click())
    }

    private fun changeArrivalDate() {

        onView(withId(R.id.nestedScrollView))
            .perform(swipeUp())
        onView(allOf(withId(R.id.dateTimeChangeButton), withParent(withId(R.id.arrivalDts))))
            .perform(click())
        onView(withClassName(equalTo(DatePicker::class.java.name)))
            .perform(PickerActions.setDate(2021, 5, 6))
        onView(withId(AlertDialogButton.POSITIVE.resId))
            .perform(click())

        onView(withClassName(equalTo(TimePicker::class.java.name)))
            .perform(PickerActions.setTime(14, 15))
        onView(withId(AlertDialogButton.POSITIVE.resId))
            .perform(click())
    }


    private fun editFlight() {
        openSecondFlightDetails()
        onView(withId(R.id.nestedScrollView))
            .perform(swipeUp(), swipeUp())
        onView(withId(R.id.editButton))
            .perform(click())

        changeDepartureRunway()
        changeArrivalRunway()
        changeDepartureDate()
        changeArrivalDate()
        onView(withId(R.id.distanceTie))
            .perform(typeText("0"), pressImeActionButton())
        onView(withId(R.id.noteTie))
            .perform(replaceText("New note from flight"), closeSoftKeyboard())

        onView(withId(R.id.saveButton)).perform(click())

        onView(allOf(withId(R.id.valueTextView), withParent(withId(R.id.airplaneEwtv))))
            .check(matches(withText("A380")))
        onView(allOf(withId(R.id.valueTextView), withParent(withId(R.id.departureAirportEwtv))))
            .check(matches(withText("Okęcie (EPWA)")))
        onView(allOf(withId(R.id.valueTextView), withParent(withId(R.id.departureRunwayEwtv))))
            .check(matches(withText("15")))

        onView(allOf(withId(R.id.valueTextView), withParent(withId(R.id.arrivalAirportEwtv))))
            .check(matches(withText("Okęcie (EPWA)")))
        onView(allOf(withId(R.id.valueTextView), withParent(withId(R.id.arrivalRunwayEwtv))))
            .check(matches(withText("11")))

        onView(allOf(withId(R.id.valueTextView), withParent(withId(R.id.departureTimeEwtv))))
            .check(matches(withText("06.05.2021 11:29")))
        onView(allOf(withId(R.id.valueTextView), withParent(withId(R.id.arrivalTimeEwtv))))
            .check(matches(withText("06.05.2021 14:15")))

        onView(allOf(withId(R.id.valueTextView), withParent(withId(R.id.distanceEwtv))))
            .check(matches(withText("1230")))
        onView(withId(R.id.noteTv))
            .check(matches(withText("New note from flight")))

    }

    private fun deleteFlight() {
        editFlight()
        onView(withId(R.id.deleteIv)).perform(click())
        onView(withId(AlertDialogButton.NEGATIVE.resId))
            .inRoot(RootMatchers.isDialog())
            .perform(click())
        onView(withId(R.id.deleteIv)).perform(click())
        onView(withId(AlertDialogButton.POSITIVE.resId))
            .inRoot(RootMatchers.isDialog())
            .perform(click())
    }

    @Test
    fun testFlightsFlow() {
        deleteFlight()
    }
}