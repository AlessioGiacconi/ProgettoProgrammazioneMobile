package com.example.progettoprogrammazionemobile.activity

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.progettoprogrammazionemobile.R
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Calendar
import java.util.Locale

class CreateEventActivity : AppCompatActivity() {

    lateinit var scegliData : EditText
    lateinit var scegliOra : EditText
    private var cal = Calendar.getInstance()
    lateinit var scegliRuoli: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        scegliOra = findViewById(R.id.et_ora)
        scegliData = findViewById(R.id.et_data)
        scegliRuoli = findViewById(R.id.multiselect_ruoli)

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
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Scegli i ruoli")
            builder.setMultiChoiceItems(listRoles, checkedRoles){ dialog, which, isChecked ->
                checkedRoles[which] = isChecked
                val currentRole = selectedRoles[which]
            }

            builder.setCancelable(false)
            builder.setPositiveButton("FATTO"){ dialog, which ->
                for (i in checkedRoles.indices){
                    if (checkedRoles[i]){
                        scegliRuoli.text = String.format("%s%s ", scegliRuoli.text, selectedRoles[i])
                    }
                }
            }
            builder.setNegativeButton("INDIETRO"){dialog, which ->}
            builder.setNeutralButton("CANCELLA"){ dialog: DialogInterface?, which: Int ->
                Arrays.fill(checkedRoles, false)
            }
            builder.create()
            val alertDialog = builder.create()
            alertDialog.show()

        }

    }

    private fun updateDateInView(){
        val myFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.ITALY)
        scegliData.setText(sdf.format(cal.time))
    }
}