package com.example.progettoprogrammazionemobile.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.progettoprogrammazionemobile.R
import java.io.FileNotFoundException
import java.io.InputStream


class RegisterActivity : AppCompatActivity() {

    lateinit var imgProfilePhoto: ImageView
    lateinit var pickedImage: Uri
    val PReqCode = 1
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

        imgProfilePhoto = findViewById(R.id.photoImageView)

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

        imgProfilePhoto.setOnClickListener(View.OnClickListener {
                if (Build.VERSION.SDK_INT >= 22){
                    checkAndRequestForPermission()
                }
                else {
                    openGallery()
                }
        })


        registrati.setOnClickListener{

        }

        tv_login.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun checkAndRequestForPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(this, "È necessario accettare i permessi per continuare", Toast.LENGTH_SHORT).show()
            }
            else{
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),PReqCode)
            }
        }
        else{
            openGallery()
        }

    }

    private fun openGallery() {
        val photoPickerIntent =  Intent(Intent.ACTION_GET_CONTENT)
        photoPickerIntent.type = "image/"
        pickPhotoActivityResultLauncher.launch(photoPickerIntent)
    }

    val pickPhotoActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {

                val data = result.data
                val selectedImage: Uri? = data?.data
                var imageStream: InputStream? = null
                try {
                    imageStream = contentResolver.openInputStream(selectedImage!!)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
                BitmapFactory.decodeStream(imageStream)
                if (data != null) {
                    pickedImage = data.getData()!!
                }
                imgProfilePhoto.setImageURI(selectedImage) // To display selected image in image view

            }
        }

}

