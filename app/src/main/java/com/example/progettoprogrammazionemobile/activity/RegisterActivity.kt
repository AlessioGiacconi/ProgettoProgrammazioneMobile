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
        val ruolo = listOf("Attaccante", "Difensore", "Portiere")
        val autoCompleteSesso : AutoCompleteTextView = findViewById(R.id.Autocomplete_text_view_sesso)
        val adapterSesso = ArrayAdapter(this, R.layout.dropdown_item, sesso)
        val autoCompleteRuolo : AutoCompleteTextView = findViewById(R.id.Autocomplete_text_view_ruolo)
        val adapterRuolo = ArrayAdapter(this, R.layout.dropdown_item, ruolo)

        autoCompleteSesso.setAdapter(adapterSesso)
        autoCompleteSesso.onItemClickListener = AdapterView.OnItemClickListener{
                adapterView, view, i, l ->

            val sessoSelezionato = adapterView.getItemAtPosition(i)
        }
        
        autoCompleteRuolo.setAdapter(adapterRuolo)
        autoCompleteRuolo.onItemClickListener = AdapterView.OnItemClickListener{
            adapterView, view, i, l ->

            val ruoloSelezionato = adapterView.getItemAtPosition(i)
        }
    }
}