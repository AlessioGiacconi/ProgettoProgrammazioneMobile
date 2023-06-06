package com.example.progettoprogrammazionemobile

import android.app.Dialog
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AlertDialog
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.contrib.PickerActions.setDate
import androidx.test.espresso.contrib.PickerActions.setTime
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.progettoprogrammazionemobile.activity.EditEventActivity
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class EditEventInstrumentedTest {

    @get:Rule
    var activityRule : ActivityScenarioRule<EditEventActivity> = ActivityScenarioRule(EditEventActivity::class.java)

    @Test
    fun checkRegisterUser(){

        onView(withId(R.id.et_edit_data_evento)).perform(click())
        onView(isAssignableFrom(DatePicker::class.java)).perform(setDate(2023, 6, 6))
        onView(withId(android.R.id.button1)).perform(click())

        onView(withId(R.id.et_edit_ora_evento)).perform(click())
        onView(isAssignableFrom(TimePicker::class.java)).perform(setTime(16, 45))
        onView(withId(android.R.id.button1)).perform(click())

        onView(withId(R.id.et_edit_luogo_evento)).perform(typeText("Numana"), closeSoftKeyboard())

        onView(withId(R.id.multiselect_edit_ruoli)).perform(typeText("Attaccante"), closeSoftKeyboard())

        onView(withId(R.id.et_edit_numero_giocatori_evento)).perform(typeText("4"), closeSoftKeyboard())

        onView(withId(R.id.et_edit_descrizione_evento)).perform(typeText("prova edit"))
    }


}