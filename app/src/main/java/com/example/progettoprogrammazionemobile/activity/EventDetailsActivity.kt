package com.example.progettoprogrammazionemobile.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.progettoprogrammazionemobile.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EventDetailsActivity : AppCompatActivity() {

    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_details)

        auth = Firebase.auth

        var nomeCreatore: String?
        var cognomeCreatore: String?
        var telefonoCreatore: String? = null
        var eventList: ArrayList<*>?

        val titolo = findViewById<TextView>(R.id.titolo)
        val creatore = findViewById<TextView>(R.id.creatore)
        val data = findViewById<TextView>(R.id.data_evento)
        val ora = findViewById<TextView>(R.id.ora_evento)
        val luogo = findViewById<TextView>(R.id.luogo)
        val ruoliEvento = findViewById<TextView>(R.id.ruoli_evento)
        val numeroGiocatori = findViewById<TextView>(R.id.numero_giocatori_cercati_evento)
        val descrizione = findViewById<TextView>(R.id.descrizione_evento)

        val evento = intent.extras

        val chiamaBtn = findViewById<Button>(R.id.telefona_btn)
        val annullaBtn = findViewById<Button>(R.id.annulla_btn)
        val partecipaBtn = findViewById<Button>(R.id.partecipa_btn)
        val whatsappBtn = findViewById<Button>(R.id.whatsapp_btn)

        if (auth.currentUser!!.email == evento!!.getString("creatore")) {
            annullaBtn.visibility = View.INVISIBLE
            partecipaBtn.visibility = View.INVISIBLE
            chiamaBtn.visibility = View.INVISIBLE
            whatsappBtn.visibility = View.INVISIBLE
        }

        db.collection("users").document(evento.getString("creatore").toString()).get()
            .addOnSuccessListener { doc ->
                nomeCreatore = doc["Nome"].toString()
                cognomeCreatore = doc["Cognome"].toString()
                telefonoCreatore = doc["Telefono"].toString()

                db.collection("users").document(auth.currentUser!!.email.toString()).get()
                    .addOnSuccessListener { doc ->
                        eventList = doc["Miei Eventi"] as ArrayList<*>?
                        Log.d("DATABASE", eventList.toString())

                        var flag = false

                        for (e in eventList!!) {
                            Log.d("EventDetails", e.toString())
                            if (evento.getString("titolo") == e) {
                                flag = true
                            }

                            if (eventList!!.isEmpty()) {
                                Log.d("EventDetails", eventList.toString())
                                partecipaBtn.visibility = View.VISIBLE
                                annullaBtn.visibility = View.INVISIBLE
                            } else {
                                if (flag) {
                                    annullaBtn.visibility = View.VISIBLE
                                    partecipaBtn.visibility = View.INVISIBLE
                                } else {
                                    partecipaBtn.visibility = View.VISIBLE
                                    annullaBtn.visibility = View.INVISIBLE
                                }
                            }
                        }
                    }

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


        partecipaBtn.setOnClickListener {
            val docUtente = db.collection("users").document(auth.currentUser!!.email.toString())
            val eventDoc = db.collection("events").document(evento.getString("titolo").toString())
            docUtente.update("Miei Eventi", FieldValue.arrayUnion(evento.getString("titolo")))
            eventDoc.update("persone_richieste", evento.getLong("persone_richieste") - 1)
            partecipaBtn.visibility = View.INVISIBLE
            annullaBtn.visibility = View.VISIBLE
            Toast.makeText(this, "Partecipazione all'evento registrata", Toast.LENGTH_SHORT).show()
            finish()
            startActivity(Intent(this, SearchEventActivity::class.java))
        }

        annullaBtn.setOnClickListener {
            val docUtente = db.collection("users").document(auth.currentUser!!.email.toString())
            val eventDoc = db.collection("events").document(evento.getString("titolo").toString())
            docUtente.update("Miei Eventi", FieldValue.arrayRemove(evento.getString("titolo")))
            eventDoc.update("persone_richieste", evento.getLong("persone_richieste") + 1)
            partecipaBtn.visibility = View.VISIBLE
            annullaBtn.visibility = View.INVISIBLE
            Toast.makeText(this, "Partecipazione all'evento annullata", Toast.LENGTH_SHORT).show()
            finish()
            startActivity(Intent(this, SearchEventActivity::class.java))
        }


        chiamaBtn.setOnClickListener {
            val phoneUri = "tel:$telefonoCreatore"
            val phoneIntent = Intent(Intent.ACTION_DIAL, Uri.parse(phoneUri))
            startActivity(phoneIntent)
        }

        whatsappBtn.setOnClickListener {
            val waLink =
                "https://wa.me/$telefonoCreatore?text=Ciao%2C%20ho%20visto%20il%20tuo%20evento%20su%20Meet%26Kick%20e%20vorrei%20partecipare%21"
            val whatsappIntent = Intent(Intent.ACTION_VIEW, Uri.parse(waLink))
            startActivity(whatsappIntent)
        }
    }
}


