package com.example.progettoprogrammazionemobile.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.progettoprogrammazionemobile.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EventDetailsActivity : AppCompatActivity() {


    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        var nomeCreatore: String?
        var cognomeCreatore: String?
        var telefonoCreatore: String? = null

        val titolo = findViewById<TextView>(R.id.titolo)
        val creatore = findViewById<TextView>(R.id.creatore)
        val data = findViewById<TextView>(R.id.data_evento)
        val ora = findViewById<TextView>(R.id.ora_evento)
        val luogo = findViewById<TextView>(R.id.luogo)
        val ruoliEvento = findViewById<TextView>(R.id.ruoli_evento)
        val numeroGiocatori = findViewById<TextView>(R.id.numero_giocatori_cercati_evento)
        val descrizione = findViewById<TextView>(R.id.descrizione_evento)

        val evento = intent.extras

        val callBtn = findViewById<Button>(R.id.telefona_btn)

        db.collection("users").document(evento!!.getString("creatore").toString()).get()
            .addOnSuccessListener { doc ->
                nomeCreatore = doc["Nome"].toString()
                cognomeCreatore = doc["Cognome"].toString()
                telefonoCreatore = doc["Telefono"].toString()

                titolo.text = getString(R.string.titolo, evento.getString("titolo"))
                creatore.text = getString(R.string.creatore, nomeCreatore, cognomeCreatore)
                data.text = evento.getString("data")
                ora.text = evento.getString("ora")
                luogo.text = evento.getString("luogo")
                ruoliEvento.text = evento.getStringArrayList("ruoli_richiesti").toString()
                    .subSequence(
                        1,
                        evento.getStringArrayList("ruoli_richiesti").toString().length - 1
                    )
                numeroGiocatori.text = evento.getLong("persone_richieste").toString()
                descrizione.text = evento.getString("descrizione")

            }.addOnFailureListener {
                Toast.makeText(this, "Errore di comunicazione col database!", Toast.LENGTH_LONG)
                    .show()
            }
        
        callBtn.setOnClickListener {
            val phoneUri = "tel:$telefonoCreatore"
            val phoneIntent = Intent(Intent.ACTION_DIAL, Uri.parse(phoneUri))
            startActivity(phoneIntent)
        }
    }
}


