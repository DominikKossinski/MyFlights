package pl.kossa.myflights.tests

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import pl.kossa.myflights.R
import pl.kossa.myflights.activities.main.MainActivity
import pl.kossa.myflights.hilt.AppModule
import pl.kossa.myflights.server.MyFlightsMockServer
import pl.kossa.myflights.utils.AlertDialogButton

@RunWith(AndroidJUnit4::class)
@LargeTest
@UninstallModules(AppModule::class)
@HiltAndroidTest
class AirplanesFlowTests {

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

    private fun openAirplanesFragment() {
        onView(withId(R.id.actionAirplanes))
            .perform(click())
    }

    @Test
    fun openAirplaneDetails() {
        openAirplanesFragment()
        onView(withId(R.id.airplanesRecyclerView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        onView(allOf(withId(R.id.valueTextView), withParent(withId(R.id.nameEwt))))
            .check(matches(withText("A380")))
        onView(allOf(withId(R.id.valueTextView), withParent(withId(R.id.maxSpeedEtw))))
            .check(matches(withText("50")))
        onView(allOf(withId(R.id.valueTextView), withParent(withId(R.id.weightEwt))))
            .check(matches(withText("50")))
    }

    private fun openAddAirplane() {
        openAirplanesFragment()
        onView(withId(R.id.fab))
            .perform(click())
    }

    private fun addAirplane() {
        openAddAirplane()
        onView(withId(R.id.addButton)).check(matches(isNotEnabled()))
        onView(withId(R.id.nameTie)).perform(typeText("A320"))
        onView(withId(R.id.maxSpeedTie)).perform(typeText("200"))
        onView(withId(R.id.weightTie)).perform(typeText("200"), closeSoftKeyboard())
        onView(withId(R.id.addButton)).check(matches(isEnabled()))
        onView(withId(R.id.addButton)).perform(click())
        onView(allOf(withId(R.id.backTv), withParent(withId(R.id.backAppbar)))).perform(click())
    }

    private fun openSecondAirplaneDetails() {
        addAirplane()
        onView(withId(R.id.airplanesRecyclerView))
            .perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(1, click()))
        onView(allOf(withId(R.id.valueTextView), withParent(withId(R.id.nameEwt))))
            .check(matches(withText("A320")))
        onView(allOf(withId(R.id.valueTextView), withParent(withId(R.id.maxSpeedEtw))))
            .check(matches(withText("200")))
        onView(allOf(withId(R.id.valueTextView), withParent(withId(R.id.weightEwt))))
            .check(matches(withText("200")))
    }

    private fun editAirplane() {
        openSecondAirplaneDetails()
        onView(withId(R.id.nestedScrollView)).perform(swipeUp())
        onView(withId(R.id.editButton)).perform(click())

        onView(withId(R.id.nameTie))
            .perform(replaceText(""))
        onView(withId(R.id.saveButton)).check(matches(isNotEnabled()))
        onView(withId(R.id.saveIv)).check(matches(isNotEnabled()))
        onView(withId(R.id.nameTie))
            .perform(typeText("A321"))
        onView(withId(R.id.saveButton)).check(matches(isEnabled()))
        onView(withId(R.id.saveIv)).check(matches(isEnabled()))


        onView(withId(R.id.maxSpeedTie))
            .perform(replaceText(""))
            .perform(typeText("750"))
            .perform(closeSoftKeyboard())
        onView(withId(R.id.saveButton)).check(matches(isNotEnabled()))
        onView(withId(R.id.saveIv)).check(matches(isNotEnabled()))
        onView(withId(R.id.maxSpeedTie))
            .perform(replaceText(""))
            .perform(typeText("250"))
            .perform(closeSoftKeyboard())
        onView(withId(R.id.saveButton)).check(matches(isEnabled()))
        onView(withId(R.id.saveIv)).check(matches(isEnabled()))

        onView(withId(R.id.weightTie))
            .perform(replaceText(""))
            .perform(typeText("750"))
            .perform(closeSoftKeyboard())
        onView(withId(R.id.saveButton)).check(matches(isNotEnabled()))
        onView(withId(R.id.saveIv)).check(matches(isNotEnabled()))
        onView(withId(R.id.weightTie))
            .perform(replaceText(""))
            .perform(typeText("250"))
            .perform(closeSoftKeyboard())
        onView(withId(R.id.saveButton)).check(matches(isEnabled()))
        onView(withId(R.id.saveIv)).check(matches(isEnabled()))


        onView(withId(R.id.saveButton)).perform(click())
        onView(allOf(withId(R.id.valueTextView), withParent(withId(R.id.nameEwt))))
            .check(matches(withText("A321")))
        onView(allOf(withId(R.id.valueTextView), withParent(withId(R.id.maxSpeedEtw))))
            .check(matches(withText("250")))
        onView(allOf(withId(R.id.valueTextView), withParent(withId(R.id.weightEwt))))
            .check(matches(withText("250")))
    }

    private fun deleteAirplane() {
        editAirplane()
        onView(withId(R.id.deleteIv)).perform(click())
        onView(withId(AlertDialogButton.NEGATIVE.resId))
            .inRoot(isDialog())
            .perform(click())
        onView(withId(R.id.deleteIv)).perform(click())
        onView(withId(AlertDialogButton.POSITIVE.resId))
            .inRoot(isDialog())
            .perform(click())
    }

    @Test
    fun testAirplanesFlow() {
        deleteAirplane()
    }

}