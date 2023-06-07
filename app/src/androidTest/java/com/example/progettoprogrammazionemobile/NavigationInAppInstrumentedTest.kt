package com.example.progettoprogrammazionemobile


import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.example.progettoprogrammazionemobile.activity.LoginActivity
import org.junit.After
import org.junit.Test

class NavigationInAppInstrumentedTest {

    private lateinit var activityScenario: ActivityScenario<MainActivity>
    private lateinit var activityScenarioLogin: ActivityScenario<LoginActivity>

    @Test
    fun testNavigationFromMainActivity() {

        activityScenarioLogin = ActivityScenario.launch((LoginActivity::class.java))

        onView(withId(R.id.email)).perform(typeText("marco@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(typeText("qwertyui"), closeSoftKeyboard())

        onView(withId(R.id.login_btn)).perform(click())

        activityScenario = ActivityScenario.launch(MainActivity::class.java)

        Thread.sleep(1000)
        onView(withId(R.id.crea_evento)).perform(click())
        onView(withId(R.id.sv_new_event)).check(matches(isDisplayed()))

        pressBack()

        onView(withId(R.id.logout)).perform(click())

    }

    @After
    fun tearDown() {
        activityScenario.close()
        activityScenarioLogin.close()
    }

}