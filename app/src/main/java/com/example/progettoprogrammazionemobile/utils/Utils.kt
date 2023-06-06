package com.example.progettoprogrammazionemobile.utils


import java.text.SimpleDateFormat
import java.util.Locale

class Utils {

    fun updateDateInView(): SimpleDateFormat {
        val myFormat = "dd.MM.yyyy"
        return SimpleDateFormat(myFormat, Locale.ITALY)
    }

}