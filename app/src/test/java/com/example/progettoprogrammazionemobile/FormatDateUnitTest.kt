package com.example.progettoprogrammazionemobile

import com.example.progettoprogrammazionemobile.utils.Utils
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FormatDateUnitTest {

    private var cal = Calendar.getInstance()


    @Before
    fun setUp(){
        cal.set(Calendar.YEAR, 2023)
        cal.set(Calendar.MONTH, 7) // dato che Java Calendar utilizza come indice per gennaio lo 0 è necessario decrementare di 1 il valore del mese corrente
        cal.set(Calendar.DAY_OF_MONTH, 6)
    }

    @Test
    fun checkDateFormat(){
        val date = Utils()
        assertEquals(SimpleDateFormat("dd.MM.yyyy",Locale.ITALY), date.updateDateInView())
        assertEquals("06.08.2023", date.updateDateInView().format(cal.time))

    }

}