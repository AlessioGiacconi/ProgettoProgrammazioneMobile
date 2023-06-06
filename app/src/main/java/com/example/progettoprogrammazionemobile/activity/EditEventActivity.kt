package com.example.progettoprogrammazionemobile.activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.progettoprogrammazionemobile.MainActivity
import com.example.progettoprogrammazionemobile.R
import com.example.progettoprogrammazionemobile.data_class.EventDataClass
import com.example.progettoprogrammazionemobile.utils.Utils
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Calendar
import java.util.Locale

class EditEventActivity : AppCompatActivity() {

    lateinit var nuovaOra: EditText
    lateinit var nuovaData: EditText
    private var cal = Calendar.getInstance()
    lateinit var nuoviRuoli: TextView
    private val db = Firebase.firestore
    private lateinit var selectedEvent: EventDataClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_event)

        val titoloEvento = intent.getStringExtra("titolo evento")

        val tvTitolo = findViewById<TextView>(R.id.tv_titolo_evento)
        nuovaData = findViewById(R.id.et_edit_data_evento)
        nuovaOra = findViewById(R.id.et_edit_ora_evento)
        val nuovoLuogo = findViewById<EditText>(R.id.et_edit_luogo_evento)
        nuoviRuoli = findViewById(R.id.multiselect_edit_ruoli)
        val nuovoNGiocatori = findViewById<EditText>(R.id.et_edit_numero_giocatori_evento)
        val nuovaDescrizione = findViewById<EditText>(R.id.et_edit_descrizione_evento)

        val confirmBtn = findViewById<ExtendedFloatingActionButton>(R.id.confirm_btn)

        val mTimePicker: TimePickerDialog
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)
        val listRoles = arrayOf("Attaccante", "Difensore", "Portiere")
        val checkedRoles = BooleanArray(listRoles.size)
        val selectedRoles = mutableListOf(*listRoles)

        mTimePicker = TimePickerDialog(
            this,
            { view, hourOfDay, minute ->
                nuovaOra.setText(
                    String.format(
                        "%d : %d",
                        hourOfDay,
                        minute
                    )
                )
            }, hour, minute, true
        )

        nuovaOra.setOnClickListener { v ->
            mTimePicker.show()
        }

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val formatDate = Utils()
                val date = formatDate.updateDateInView()
                nuovaData.setText(date.format(cal.time))
            }

        nuovaData.setOnClickListener(View.OnClickListener {
            DatePickerDialog(
                this, dateSetListener, cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        })

        nuoviRuoli.setOnClickListener {

            nuoviRuoli.text = ""
            val builderRuoli = AlertDialog.Builder(this)
            builderRuoli.setTitle("Scegli i ruoli")
            builderRuoli.setMultiChoiceItems(listRoles, checkedRoles) { dialog, which, isChecked ->
                checkedRoles[which] = isChecked
                val currentRole = selectedRoles[which]
            }

            builderRuoli.setCancelable(false)
            builderRuoli.setPositiveButton("FATTO") { dialog, which ->
                for (i in checkedRoles.indices) {
                    if (checkedRoles[i]) {
                        nuoviRuoli.text = String.format("%s%s ", nuoviRuoli.text, selectedRoles[i])
                    }
                }
            }

            builderRuoli.setNegativeButton("INDIETRO") { dialog, which -> }
            builderRuoli.setNeutralButton("CANCELLA") { dialog: DialogInterface?, which: Int ->
                Arrays.fill(checkedRoles, false)
            }
            builderRuoli.create()
            val alertDialog = builderRuoli.create()
            alertDialog.show()

        }

        db.collection("events").document(titoloEvento.toString()).get()
            .addOnSuccessListener { doc ->
                selectedEvent = EventDataClass(
                    doc["titolo"].toString(),
                    doc["data"].toString(),
                    doc["ora"].toString(),
                    doc["luogo"].toString(),
                    doc["ruoli_richiesti"] as List<String>?,
                    doc["persone_richieste"] as Long?,
                    doc["descrizione"].toString(),
                    doc["creatore"].toString()
                )

                Log.d("EditEvent", doc["ruoli_richiesti"].toString())

                tvTitolo.text = titoloEvento
                nuovaData.setText(selectedEvent.data)
                nuovaOra.setText(selectedEvent.ora)
                nuovoLuogo.setText(selectedEvent.luogo)
                nuoviRuoli.text = selectedEvent.ruoli_richiesti.toString().subSequence(
                    1,
                    selectedEvent.ruoli_richiesti.toString().length - 1
                )
                nuovoNGiocatori.setText(selectedEvent.persone_richieste.toString())
                nuovaDescrizione.setText(selectedEvent.descrizione)

            }.addOnFailureListener {
                Toast.makeText(this, "Errore di comunicazione col database", Toast.LENGTH_SHORT)
                    .show()
            }

        confirmBtn.setOnClickListener(View.OnClickListener {
            if (nuovaData.text.toString().isNotEmpty() && nuovaOra.text.toString()
                    .isNotEmpty() && nuovoLuogo.text.toString()
                    .isNotEmpty() && nuoviRuoli.text.toString()
                    .isNotEmpty() && nuovoNGiocatori.text.toString()
                    .isNotEmpty() && nuovaDescrizione.text.toString().isNotEmpty()
            ) {
                if (nuovoNGiocatori.text.toString().toLong() in 1..9) {
                    val editedEvent = hashMapOf(
                        "creatore" to selectedEvent.creatore.toString(),
                        "titolo" to selectedEvent.titolo.toString(),
                        "data" to nuovaData.text.toString(),
                        "ora" to nuovaOra.text.toString(),
                        "luogo" to nuovoLuogo.text.toString(),
                        "ruoli_richiesti" to nuoviRuoli.text.split(" ").dropLast(1),
                        "persone_richieste" to nuovoNGiocatori.text.toString().toLong(),
                        "descrizione" to nuovaDescrizione.text.toString()
                    )
                    Log.d("EditEvent", "event hash map: $editedEvent")
                    db.collection("events").document(selectedEvent.titolo.toString()).update(editedEvent).addOnSuccessListener {
                        Toast.makeText(this, "Informazioni modificate con successo", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }.addOnFailureListener {
                        Toast.makeText(this, "Qualcosa è andato storto durante la modifica", Toast.LENGTH_SHORT).show()

                    }
                }else {
                    Toast.makeText(this, "Devi inserire un numero di giocatori compreso tra 1 e 9", Toast.LENGTH_SHORT).show()
                }

            }else {
                Toast.makeText(this, "Ricontrolla di aver compilato tutti i campi", Toast.LENGTH_SHORT).show()
            }

        })

    }
}