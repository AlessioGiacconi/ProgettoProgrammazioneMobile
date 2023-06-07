package com.example.progettoprogrammazionemobile

import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.PickerActions.setDate
import androidx.test.espresso.contrib.PickerActions.setTime
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.progettoprogrammazionemobile.activity.CreateEventActivity
import com.example.progettoprogrammazionemobile.activity.LoginActivity
import com.google.android.material.slider.Slider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.hamcrest.Matcher
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NavigationInAppInstrumentedTest {

    private lateinit var auth: FirebaseAuth

    @Test
    fun testNavigationFromMainActivity() {

        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.crea_evento)).perform(click())
        onView(withId(R.id.sv_new_event)).check(matches(isDisplayed()))
    }


    @Test
    fun testCreateEventForm() {

        auth = Firebase.auth
        val activityScenarioStart = ActivityScenario.launch(LoginActivity::class.java)

        try {
            onView(withId(R.id.email)).perform(typeText("marco@gmail.com"), closeSoftKeyboard())
            onView(withId(R.id.password)).perform(typeText("qwertyui"), closeSoftKeyboard())
            onView(withId(R.id.login_btn)).perform(click())
        } catch (e: NoMatchingViewException) {
            e.printStackTrace()
        }
        if (auth.currentUser != null) {
            Thread.sleep(5000)
            val activityScenarioAfterLogin = ActivityScenario.launch(CreateEventActivity::class.java)

            onView(withId(R.id.et_titolo)).perform(typeText("test evento"), closeSoftKeyboard())

            onView(withId(R.id.et_data)).perform(click())
            onView(isAssignableFrom(DatePicker::class.java)).perform(setDate(2023, 8, 8))
            onView(withId(android.R.id.button1)).perform(click())

            onView(withId(R.id.et_ora)).perform(click())
            onView(isAssignableFrom(TimePicker::class.java)).perform(setTime(18, 45))
            onView(withId(android.R.id.button1)).perform(click())

            onView(withId(R.id.et_luogo)).perform((typeText("esempio")), closeSoftKeyboard())

            onView(withId(R.id.multiselect_ruoli)).perform(click())
            onView(withText("Scegli i ruoli")).check(matches(isDisplayed()))
            onView(withText("Attaccante")).perform(click())
            onView(withText("Difensore")).perform(click())
            onView(withId(android.R.id.button1)).perform(click())

            onView(withId(R.id.slider_giocatori)).perform(setValue(4))

            onView(withId(R.id.et_descrizione)).perform(
                typeText("descrizione di prova per test"),
                closeSoftKeyboard()
            )
            assertEquals("marco@gmail.com", auth.currentUser!!.email)
        }

    }

    fun setValue(value: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String {
                return "Set Slider value to $value"
            }

            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(Slider::class.java)
            }

            override fun perform(uiController: UiController?, view: View) {
                val seekBar = view as Slider
                seekBar.value = value.toFloat()
            }
        }
    }

}