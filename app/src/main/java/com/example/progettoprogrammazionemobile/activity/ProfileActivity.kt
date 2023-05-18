package com.example.progettoprogrammazionemobile.activity

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.progettoprogrammazionemobile.R
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton



class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val fabEdit = findViewById<ExtendedFloatingActionButton>(R.id.edit_btn)

        fabEdit.bringToFront()
        fabEdit.setOnClickListener(View.OnClickListener {
            Log.d("ProfileActivity", "cliccato")

            fabEdit.backgroundTintList = ColorStateList.valueOf(Color.rgb(234,255,221))
        })
    }
}