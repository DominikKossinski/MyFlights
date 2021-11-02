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
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.theories.Theories
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
class AirportsFlowTests {

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

    private fun openAirportsFragment() {
        onView(withId(R.id.actionAirports))
            .perform(click())
    }

    @Test
    fun openAirportDetails() {
        openAirportsFragment()
        onView(withId(R.id.airportsRecyclerView))
            .perform(
                actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )
    }

    private fun openAddAirport() {
        openAirportsFragment()
        onView(withId(R.id.fab))
            .perform(click())
    }

    private fun addAirport() {
        openAddAirport()
        onView(withId(R.id.addButton))
            .check(matches(isNotEnabled()))
        onView(withId(R.id.saveIv))
            .check(matches(isNotEnabled()))

        onView(withId(R.id.nameTie)).perform(typeText("Pyrzowice"), pressImeActionButton())
        onView(withId(R.id.cityTie)).perform(typeText("Katowice"), pressImeActionButton())
        onView(withId(R.id.icaoCodeTie)).perform(typeText("EPKT"), pressImeActionButton())

        onView(withId(R.id.addButton))
            .check(matches(isEnabled()))
        onView(withId(R.id.saveIv))
            .check(matches(isEnabled()))

        onView(withId(R.id.towerFrequencyTie)).perform(typeText("102.125"), pressImeActionButton())
        onView(withId(R.id.groundFrequencyTie)).perform(typeText("119.25"), pressImeActionButton())

        onView(withId(R.id.nestedScroolView)).perform(swipeUp())
        onView(withId(R.id.addButton)).perform(click())
        Thread.sleep(10_000)
        onView(withId(R.id.backTv)).perform(click())
    }


    private fun openSecondAirportDetails() {
        addAirport()
        onView(withId(R.id.airportsRecyclerView))
            .perform(
                actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1,
                    click()
                )
            )
        onView(
            Matchers.allOf(
                withId(R.id.valueTextView),
                withParent(withId(R.id.nameEwt))
            )
        )
            .check(matches(withText("Pyrzowice")))
        onView(
            Matchers.allOf(
                withId(R.id.valueTextView),
                withParent(withId(R.id.cityEwt))
            )
        )
            .check(matches(withText("Katowice")))
        onView(
            Matchers.allOf(
                withId(R.id.valueTextView),
                withParent(withId(R.id.icaoCodeEwt))
            )
        )
            .check(matches(withText("EPKT")))

        onView(
            Matchers.allOf(
                withId(R.id.valueTextView),
                withParent(withId(R.id.towerFrequencyEwt))
            )
        )
            .check(matches(withText("102.125")))
        onView(
            Matchers.allOf(
                withId(R.id.valueTextView),
                withParent(withId(R.id.groundFrequencyEwt))
            )
        )
            .check(matches(withText("119.25")))
    }

    private fun editAirport() {
        openSecondAirportDetails()
        onView(withId(R.id.nestedScroolView)).perform(swipeUp())
        Thread.sleep(5000)
        onView(withId(R.id.editButton)).perform(click())

        onView(withId(R.id.nameTie))
            .perform(replaceText(""))
        onView(withId(R.id.saveButton))
            .check(matches(isNotEnabled()))
        onView(withId(R.id.saveIv))
            .check(matches(isNotEnabled()))
        onView(withId(R.id.nameTie))
            .perform(typeText("A321"))
        onView(withId(R.id.saveButton))
            .check(matches(isEnabled()))
        onView(withId(R.id.saveIv))
            .check(matches(isEnabled()))


        onView(withId(R.id.maxSpeedTie))
            .perform(replaceText(""))
            .perform(typeText("750"))
            .perform(closeSoftKeyboard())
        onView(withId(R.id.saveButton))
            .check(matches(isNotEnabled()))
        onView(withId(R.id.saveIv))
            .check(matches(isNotEnabled()))
        onView(withId(R.id.maxSpeedTie))
            .perform(replaceText(""))
            .perform(typeText("250"))
            .perform(closeSoftKeyboard())
        onView(withId(R.id.saveButton))
            .check(matches(isEnabled()))
        onView(withId(R.id.saveIv))
            .check(matches(isEnabled()))

        onView(withId(R.id.weightTie))
            .perform(replaceText(""))
            .perform(typeText("750"))
            .perform(closeSoftKeyboard())
        onView(withId(R.id.saveButton))
            .check(matches(isNotEnabled()))
        onView(withId(R.id.saveIv))
            .check(matches(isNotEnabled()))
        onView(withId(R.id.weightTie))
            .perform(replaceText(""))
            .perform(typeText("250"))
            .perform(closeSoftKeyboard())
        onView(withId(R.id.saveButton))
            .check(matches(isEnabled()))
        onView(withId(R.id.saveIv))
            .check(matches(isEnabled()))


        onView(withId(R.id.saveButton)).perform(click())
        onView(
            Matchers.allOf(
                withId(R.id.valueTextView),
                withParent(withId(R.id.nameEwt))
            )
        )
            .check(matches(withText("A321")))
        onView(
            Matchers.allOf(
                withId(R.id.valueTextView),
                withParent(withId(R.id.maxSpeedEtw))
            )
        )
            .check(matches(withText("250")))
        onView(
            Matchers.allOf(
                withId(R.id.valueTextView),
                withParent(withId(R.id.weightEwt))
            )
        )
            .check(matches(withText("250")))
    }

    private fun deleteAirport() {
        editAirport()
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
    fun testAirportsFlow() {
        deleteAirport()
    }


}