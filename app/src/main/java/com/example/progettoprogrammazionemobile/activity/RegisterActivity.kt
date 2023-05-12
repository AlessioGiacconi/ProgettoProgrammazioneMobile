package com.example.progettoprogrammazionemobile.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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

        val registrati = findViewById<Button>(R.id.register_btn)
        val tv_login = findViewById<TextView>(R.id.effettua_login)

        val nome = findViewById<EditText>(R.id.et_nome)
        val cognome = findViewById<EditText>(R.id.et_cognome)
        val telefono = findViewById<EditText>(R.id.et_telefono)
        val data_di_nascita = findViewById<EditText>(R.id.et_età)
        val email = findViewById<EditText>(R.id.et_email)
        val password = findViewById<EditText>(R.id.et_password)
        val conferma_password = findViewById<EditText>(R.id.et_Conferma_password)

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

        tv_login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        registrati.setOnClickListener{

        }
    }
}