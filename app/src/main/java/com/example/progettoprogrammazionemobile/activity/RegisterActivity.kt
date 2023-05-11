package com.example.progettoprogrammazionemobile.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.progettoprogrammazionemobile.R

class RegisterActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val sesso = listOf("Maschio", "Femmina")
        val autoComplete : AutoCompleteTextView = findViewById(R.id.Autocomplete_text_view_sesso)
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, sesso)

        autoComplete.setAdapter(adapter)
        autoComplete.onItemClickListener = AdapterView.OnItemClickListener{
                adapterView, view, i, l ->

            val itemSelected = adapterView.getItemAtPosition(i)
        }
    }
}