package pl.kossa.myflights.tests

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
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
class RunwaysFlowTests {

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

    private fun openAirportDetails() {
        openAirportsFragment()
        onView(withId(R.id.airportsRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )
    }

    @Test
    fun openRunwayDetails() {
        openAirportDetails()
        onView(withId(R.id.nestedScrollView)).perform(swipeUp())
        onView(withId(R.id.runwaysRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
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
            .check(ViewAssertions.matches(isNotEnabled()))
        onView(withId(R.id.saveIv))
            .check(ViewAssertions.matches(isNotEnabled()))

        onView(withId(R.id.nameTie)).perform(
            typeText("Pyrzowice"),
            pressImeActionButton()
        )
        onView(withId(R.id.cityTie)).perform(
            typeText("Katowice"),
            pressImeActionButton()
        )
        onView(withId(R.id.icaoCodeTie)).perform(
            typeText("EPKT"),
            pressImeActionButton()
        )

        onView(withId(R.id.addButton))
            .check(ViewAssertions.matches(isEnabled()))
        onView(withId(R.id.saveIv))
            .check(ViewAssertions.matches(isEnabled()))

        onView(withId(R.id.towerFrequencyTie)).perform(
            typeText("102.125"),
            pressImeActionButton()
        )
        onView(withId(R.id.groundFrequencyTie)).perform(
            typeText("119.25"),
            pressImeActionButton()
        )

        onView(withId(R.id.nestedScrollView)).perform(swipeUp())
        onView(withId(R.id.addButton)).perform(click())

        onView(withId(R.id.backTv)).perform(click())
    }

    private fun openSecondAirportDetails() {
        addAirport()
        onView(withId(R.id.airportsRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
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
            .check(ViewAssertions.matches(withText("Pyrzowice")))
        onView(
            Matchers.allOf(
                withId(R.id.valueTextView),
                withParent(withId(R.id.cityEwt))
            )
        )
            .check(ViewAssertions.matches(withText("Katowice")))
        onView(
            Matchers.allOf(
                withId(R.id.valueTextView),
                withParent(withId(R.id.icaoCodeEwt))
            )
        )
            .check(ViewAssertions.matches(withText("EPKT")))

        onView(
            Matchers.allOf(
                withId(R.id.valueTextView),
                withParent(withId(R.id.towerFrequencyEwt))
            )
        )
            .check(ViewAssertions.matches(withText("102.125")))
        onView(
            Matchers.allOf(
                withId(R.id.valueTextView),
                withParent(withId(R.id.groundFrequencyEwt))
            )
        )
            .check(ViewAssertions.matches(withText("119.25")))
    }

    private fun addRunway() {
        openSecondAirportDetails()
        onView(withId(R.id.nestedScrollView)).perform(swipeUp())
        onView(withId(R.id.addRunwayButton)).perform(click())

        onView(withId(R.id.addButton))
            .check(ViewAssertions.matches(isNotEnabled()))
        onView(withId(R.id.saveIv))
            .check(ViewAssertions.matches(isNotEnabled()))

        onView(withId(R.id.nameTie)).perform(
            typeText("11"),
            pressImeActionButton()
        )
        onView(withId(R.id.lengthTie)).perform(
            typeText("1500"),
            pressImeActionButton()
        )
        onView(withId(R.id.headingTie)).perform(
            typeText("110"),
            pressImeActionButton()
        )

        onView(withId(R.id.addButton))
            .check(ViewAssertions.matches(isEnabled()))
        onView(withId(R.id.saveIv))
            .check(ViewAssertions.matches(isEnabled()))


        onView(withId(R.id.ilsFrequencyTie)).perform(
            typeText("110.5"),
            pressImeActionButton()
        )


        onView(withId(R.id.nestedScrollView)).perform(swipeUp())
        onView(withId(R.id.addButton))
            .perform(click())

        onView(withId(R.id.backTv)).perform(click())
    }

    private fun openAddedRunwayDetails() {
        addRunway()
        onView(withId(R.id.nestedScrollView)).perform(swipeUp())
        onView(withId(R.id.runwaysRecyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )

        onView(
            Matchers.allOf(
                withId(R.id.valueTextView),
                withParent(withId(R.id.nameEwt))
            )
        )
            .check(ViewAssertions.matches(withText("11")))

        onView(
            Matchers.allOf(
                withId(R.id.valueTextView),
                withParent(withId(R.id.lengthEwt))
            )
        )
            .check(ViewAssertions.matches(withText("1500")))

        onView(
            Matchers.allOf(
                withId(R.id.valueTextView),
                withParent(withId(R.id.headingEwt))
            )
        )
            .check(ViewAssertions.matches(withText("110")))
        onView(
            Matchers.allOf(
                withId(R.id.valueTextView),
                withParent(withId(R.id.ilsFrequencyEwt))
            )
        )
            .check(ViewAssertions.matches(withText("110.5")))
    }

    private fun editRunway() {
        openAddedRunwayDetails()
        onView(withId(R.id.nestedScrollView)).perform(swipeUp())
        onView(withId(R.id.editButton)).perform(click())

        onView(withId(R.id.nameTie))
            .perform(replaceText(""))
        onView(withId(R.id.saveButton))
            .check(ViewAssertions.matches(isNotEnabled()))
        onView(withId(R.id.saveIv))
            .check(ViewAssertions.matches(isNotEnabled()))
        onView(withId(R.id.nameTie))
            .perform(typeText("12"), pressImeActionButton())
        onView(withId(R.id.saveButton))
            .check(ViewAssertions.matches(isEnabled()))
        onView(withId(R.id.saveIv))
            .check(ViewAssertions.matches(isEnabled()))


        onView(withId(R.id.lengthTie))
            .perform(replaceText(""))
        onView(withId(R.id.saveButton))
            .check(ViewAssertions.matches(isNotEnabled()))
        onView(withId(R.id.saveIv))
            .check(ViewAssertions.matches(isNotEnabled()))
        onView(withId(R.id.lengthTie))
            .perform(typeText("1200"), pressImeActionButton())
        onView(withId(R.id.saveButton))
            .check(ViewAssertions.matches(isEnabled()))
        onView(withId(R.id.saveIv))
            .check(ViewAssertions.matches(isEnabled()))


        onView(withId(R.id.headingTie))
            .perform(replaceText(""))
        onView(withId(R.id.saveButton))
            .check(ViewAssertions.matches(isNotEnabled()))
        onView(withId(R.id.saveIv))
            .check(ViewAssertions.matches(isNotEnabled()))
        onView(withId(R.id.headingTie))
            .perform(typeText("125"), pressImeActionButton())
        onView(withId(R.id.saveButton))
            .check(ViewAssertions.matches(isEnabled()))
        onView(withId(R.id.saveIv))
            .check(ViewAssertions.matches(isEnabled()))

        onView(withId(R.id.ilsFrequencyTie))
            .perform(replaceText(""))
        onView(withId(R.id.ilsFrequencyTie))
            .perform(typeText("120.5"), pressImeActionButton())

        onView(withId(R.id.nestedScrollView)).perform(swipeUp())
        onView(withId(R.id.saveButton)).perform(click())

        onView(
            Matchers.allOf(
                withId(R.id.valueTextView),
                withParent(withId(R.id.nameEwt))
            )
        )
            .check(ViewAssertions.matches(withText("12")))

        onView(
            Matchers.allOf(
                withId(R.id.valueTextView),
                withParent(withId(R.id.lengthEwt))
            )
        )
            .check(ViewAssertions.matches(withText("1200")))

        onView(
            Matchers.allOf(
                withId(R.id.valueTextView),
                withParent(withId(R.id.headingEwt))
            )
        )
            .check(ViewAssertions.matches(withText("125")))
        onView(
            Matchers.allOf(
                withId(R.id.valueTextView),
                withParent(withId(R.id.ilsFrequencyEwt))
            )
        )
            .check(ViewAssertions.matches(withText("120.5")))
    }

    private fun deleteRunway() {
        editRunway()
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
    fun testRunwaysFlow() {
        deleteRunway()
    }
}