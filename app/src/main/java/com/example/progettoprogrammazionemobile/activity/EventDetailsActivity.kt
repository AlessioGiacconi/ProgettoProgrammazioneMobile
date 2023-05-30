package com.example.progettoprogrammazionemobile.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.TextView
import com.example.progettoprogrammazionemobile.R

class EventDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        val evento = intent.extras
        if(evento == null){
            Log.d("EventDetailsActivity", "null")
        }else{
            Log.d("EventDetailsActivity", evento.getString("titolo").toString())
        }


    }
}