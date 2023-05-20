package com.example.progettoprogrammazionemobile.activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.LayoutInflaterCompat
import com.example.progettoprogrammazionemobile.R
import com.google.android.material.slider.Slider
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
    lateinit var descrizione : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        scegliOra = findViewById(R.id.et_ora)
        scegliData = findViewById(R.id.et_data)
        scegliRuoli = findViewById(R.id.multiselect_ruoli)
        numeroGiocatori = findViewById(R.id.slider_giocatori)
        descrizione = findViewById(R.id.tv_descrizione)

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

        // eventuali listener per lo slider del numero giocatori

        numeroGiocatori.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being started
            }

            override fun onStopTrackingTouch(slider: Slider) {
                // Responds to when slider's touch event is being stopped
            }
        })

        numeroGiocatori.addOnChangeListener { slider, value, fromUser ->
            // Responds to when slider's value is changed
        }

        // blocco che gestisce il dialog della descrizione


        descrizione.movementMethod = ScrollingMovementMethod()

    }

    private fun updateDateInView(){
        val myFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.ITALY)
        scegliData.setText(sdf.format(cal.time))
    }
}