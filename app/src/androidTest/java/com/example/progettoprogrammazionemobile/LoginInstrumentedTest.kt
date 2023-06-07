package com.example.progettoprogrammazionemobile

import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.progettoprogrammazionemobile.activity.LoginActivity
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTest {

    @get:Rule
    var activityRule: ActivityScenarioRule<LoginActivity> =
        ActivityScenarioRule(LoginActivity::class.java)

    @Test
    fun checkLoginUser() {
        onView(withId(R.id.email)).perform(typeText("marco@gmail.com"), closeSoftKeyboard())
        onView(withId(R.id.password)).perform(typeText("qwertyui"), closeSoftKeyboard())

        onView(withId(R.id.login_btn)).perform(click())

        onView(isAssignableFrom(Toolbar::class.java)).check(matches(isDisplayed())) // verifico la predenza della Toolbar della MainActivity, normalmente presente in quella schermata
    }
}