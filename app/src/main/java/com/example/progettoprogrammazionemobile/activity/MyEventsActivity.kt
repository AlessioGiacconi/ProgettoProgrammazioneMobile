package com.example.progettoprogrammazionemobile.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.progettoprogrammazionemobile.R
import com.example.progettoprogrammazionemobile.fragments.AttendedEventsFragment
import com.example.progettoprogrammazionemobile.fragments.CreatedEventsFragment

class MyEventsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_events)


        val myEventsButton = findViewById<Button>(R.id.eventi_creati_btn)
        val attendedEventsButton = findViewById<Button>(R.id.eventi_a_cui_partecipo_btn)
        val tvTitolo = findViewById<TextView>(R.id.textView)

        myEventsButton.setOnClickListener(View.OnClickListener {
            replaceFragment(CreatedEventsFragment())
            tvTitolo.setText(R.string.eventi_creati)
        })

        attendedEventsButton.setOnClickListener(View.OnClickListener {
            replaceFragment(AttendedEventsFragment())
            tvTitolo.setText(R.string.eventi_a_cui_partecipo)
        })

    }

    private fun replaceFragment(fragment: Fragment) {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_layout, fragment)
        transaction.commit()

    }
}