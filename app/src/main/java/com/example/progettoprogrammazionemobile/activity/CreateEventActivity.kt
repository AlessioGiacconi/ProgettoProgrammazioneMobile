package com.example.progettoprogrammazionemobile.activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.progettoprogrammazionemobile.MainActivity
import com.example.progettoprogrammazionemobile.R
import com.google.android.material.slider.Slider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Calendar
import java.util.Locale

class CreateEventActivity : AppCompatActivity() {

    lateinit var scegliData : EditText
    lateinit var scegliOra : EditText
    private var cal = Calendar.getInstance()
    lateinit var scegliRuoli: TextView
    lateinit var numeroGiocatori: Slider
    lateinit var descrizione : EditText

    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        auth = Firebase.auth

        val titolo = findViewById<EditText>(R.id.et_titolo)
        scegliOra = findViewById(R.id.et_ora)
        scegliData = findViewById(R.id.et_data)
        val scegliLuogo = findViewById<EditText>(R.id.et_luogo)
        scegliRuoli = findViewById(R.id.multiselect_ruoli)
        numeroGiocatori = findViewById(R.id.slider_giocatori)
        descrizione = findViewById(R.id.et_descrizione)
        var valoreSlider = 1

        val conferma = findViewById<Button>(R.id.confirm_btn)

        descrizione.movementMethod = ScrollingMovementMethod()

        val mTimePicker: TimePickerDialog
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
        val minute = mcurrentTime.get(Calendar.MINUTE)
        val listRoles = arrayOf("Attaccante", "Difensore", "Portiere")
        val checkedRoles = BooleanArray(listRoles.size)
        val selectedRoles = mutableListOf(*listRoles)

        // blocco che gestisce il time picker
        mTimePicker = TimePickerDialog(this,
            { view, hourOfDay, minute -> scegliOra.setText(String.format("%d : %d", hourOfDay, minute)) }, hour, minute, true)

        scegliOra.setOnClickListener { v ->
            mTimePicker.show()
        }

        // blocco che gestisce il date picker
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }

        scegliData.setOnClickListener(View.OnClickListener {
            DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        })

        // blocco che gestisce l'apertura del dialog per selezionare i ruoli
        scegliRuoli.setOnClickListener{

            scegliRuoli.text = ""
            val builderRuoli = AlertDialog.Builder(this)
            builderRuoli.setTitle("Scegli i ruoli")
            builderRuoli.setMultiChoiceItems(listRoles, checkedRoles){ dialog, which, isChecked ->
                checkedRoles[which] = isChecked
                val currentRole = selectedRoles[which]
            }

            builderRuoli.setCancelable(false)
            builderRuoli.setPositiveButton("FATTO"){ dialog, which ->
                for (i in checkedRoles.indices){
                    if (checkedRoles[i]){
                        scegliRuoli.text = String.format("%s%s ", scegliRuoli.text, selectedRoles[i])
                    }
                }
            }
            builderRuoli.setNegativeButton("INDIETRO"){dialog, which ->}
            builderRuoli.setNeutralButton("CANCELLA"){ dialog: DialogInterface?, which: Int ->
                Arrays.fill(checkedRoles, false)
            }
            builderRuoli.create()
            val alertDialog = builderRuoli.create()
            alertDialog.show()

        }

        // listener per lo slider del numero giocatori
        numeroGiocatori.addOnChangeListener { slider, value, fromUser ->
            valoreSlider = numeroGiocatori.value.toInt()
        }



        conferma.setOnClickListener(View.OnClickListener {
            if(scegliData.text.toString().isNotEmpty() && scegliOra.text.toString().isNotEmpty() && scegliLuogo.text.toString().isNotEmpty() && scegliRuoli.text.toString().isNotEmpty()
                && descrizione.text.toString().isNotEmpty()){
                val event = hashMapOf(
                    "creatore" to auth.currentUser!!.email.toString(),
                    "titolo" to titolo.text.toString(),
                    "data" to scegliData.text.toString(),
                    "ora" to scegliOra.text.toString(),
                    "luogo" to scegliLuogo.text.toString(),
                    "ruoli_richiesti" to scegliRuoli.text.split(" ").dropLast(1),
                    "persone_richieste" to valoreSlider,
                    "descrizione" to descrizione.text.toString()
                )
                Log.d("CreateActivity", "ok")
                Log.d("CreateActivity", "Event hash map:$event")
                db.collection("events").document(event["titolo"].toString()).set(event).addOnSuccessListener {
                    showMessage("Evento creato con successo")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }.addOnFailureListener {
                    showMessage("Qualcosa è andato storto...")
                }
            }else{
              showMessage("Compila tutti i campi per pubblicare il nuovo evento")
            }
        })

    }

    private fun updateDateInView(){
        val myFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.ITALY)
        scegliData.setText(sdf.format(cal.time))
    }

    private fun showMessage(messaggio: String) {
        Toast.makeText(this, messaggio, Toast.LENGTH_LONG).show()
    }


}