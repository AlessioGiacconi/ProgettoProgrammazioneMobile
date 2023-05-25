package com.example.progettoprogrammazionemobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.progettoprogrammazionemobile.activity.CreateEventActivity
import com.example.progettoprogrammazionemobile.activity.LoginActivity
import com.example.progettoprogrammazionemobile.activity.MyEventsActivity
import com.example.progettoprogrammazionemobile.activity.ProfileActivity
import com.example.progettoprogrammazionemobile.activity.SearchEventActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        val visualizzaEventi = findViewById<Button>(R.id.button_visualizza_eventi)
        val cercaEventi = findViewById<Button>(R.id.button_cerca_eventi)
        val bottomBar = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        visualizzaEventi.setOnClickListener{
            val myEventsActivity = Intent(this,MyEventsActivity::class.java)
            startActivity(myEventsActivity)
        }

        cercaEventi.setOnClickListener{
            val searchEventActivity = Intent(this, SearchEventActivity::class.java)
            startActivity(searchEventActivity)
        }

        bottomBar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.modifica_account -> {
                    val profileActivity = Intent(this, ProfileActivity::class.java)
                    startActivity(profileActivity)
                    true
                }
                R.id.crea_evento -> {
                    val createEventActivity = Intent(this, CreateEventActivity::class.java)
                    startActivity(createEventActivity)
                    true
                }
                R.id.logout -> {
                    Firebase.auth.signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                    true
                }

                else -> {

                }
            }
            true
        }

    }
}