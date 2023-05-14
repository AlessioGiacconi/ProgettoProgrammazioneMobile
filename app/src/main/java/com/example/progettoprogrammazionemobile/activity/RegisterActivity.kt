package com.example.progettoprogrammazionemobile.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.progettoprogrammazionemobile.R
import com.example.progettoprogrammazionemobile.data_class.UserDataClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.FileNotFoundException
import java.io.InputStream


class RegisterActivity : AppCompatActivity() {

    lateinit var imgProfilePhoto: ImageView
    lateinit var pickedImage: Uri
    val PReqCode = 1
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth

        val sesso = listOf("Maschio", "Femmina")
        val ruolo = listOf("Attaccante", "Difensore", "Portiere")
        val autoCompleteSesso : AutoCompleteTextView = findViewById(R.id.Autocomplete_text_view_sesso)
        val adapterSesso = ArrayAdapter(this, R.layout.dropdown_item, sesso)
        val autoCompleteRuolo : AutoCompleteTextView = findViewById(R.id.Autocomplete_text_view_ruolo)
        val adapterRuolo = ArrayAdapter(this, R.layout.dropdown_item, ruolo)
        autoCompleteSesso.setAdapter(adapterSesso)
        autoCompleteRuolo.setAdapter(adapterRuolo)

        val registrati = findViewById<Button>(R.id.register_btn)
        val tv_login = findViewById<TextView>(R.id.effettua_login)
        val loadingProgress = findViewById<ProgressBar>(R.id.progressBar)
        loadingProgress.visibility = View.INVISIBLE

        val userNome = findViewById<EditText>(R.id.et_nome)
        val userCognome = findViewById<EditText>(R.id.et_cognome)
        val userTelefono = findViewById<EditText>(R.id.et_telefono)
        val userDataDiNascita = findViewById<EditText>(R.id.et_età)
        val userSesso = autoCompleteSesso.onItemSelectedListener.toString()
        val userRuolo = autoCompleteRuolo.onItemSelectedListener.toString()
        val userEmail = findViewById<EditText>(R.id.et_email)
        val userPassword = findViewById<EditText>(R.id.et_password)
        val userConfermaPassword = findViewById<EditText>(R.id.et_Conferma_password)
        imgProfilePhoto = findViewById(R.id.photoImageView)

        //autoCompleteSesso.onItemClickListener = AdapterView.OnItemClickListener{
        //        adapterView, view, i, l ->

        //    val userSesso = adapterView.getItemAtPosition(i)
        //}

        //autoCompleteRuolo.onItemClickListener = AdapterView.OnItemClickListener{
        //    adapterView, view, i, l ->

        //    val userRuolo = adapterView.getItemAtPosition(i)
        //}

        imgProfilePhoto.setOnClickListener(View.OnClickListener {
                if (Build.VERSION.SDK_INT >= 22){
                    checkAndRequestForPermission()
                }
                else {
                    openGallery()
                }
        })

        val db = Firebase.firestore

        registrati.setOnClickListener(View.OnClickListener{
            registrati.visibility = View.INVISIBLE
            loadingProgress.visibility = View.INVISIBLE

            if( userNome.text.toString().isNotEmpty() && userCognome.text.toString().isNotEmpty() && userTelefono.text.toString().isNotEmpty() &&
                userDataDiNascita.text.toString().isNotEmpty() && userSesso.isNotEmpty() && userRuolo.isNotEmpty() && userEmail.text.toString().isNotEmpty() &&
                userPassword.text.toString().isNotEmpty() && userConfermaPassword.text.toString().isNotEmpty() && userPassword.text.toString().length >= 8
                && userPassword.text.toString() == userConfermaPassword.text.toString()) {
                if(userPassword.text.toString().length >= 8){
                    val user = hashMapOf(
                        "Nome" to userNome.text.toString(),
                        "Cognome" to userCognome.text.toString(),
                        "Telefono" to userTelefono.text.toString(),
                        "Data di Nascita" to userDataDiNascita.text.toString(),
                        "Sesso" to userSesso,
                        "Ruolo" to userRuolo,
                        "Email" to userEmail.text.toString(),
                        "Password" to userPassword.text.toString()
                    )
                    CreateUserAccount(user)

                }
                else{
                    showMessage("La password deve contenere almeno 8 caratteri")
                    registrati.visibility = View.VISIBLE
                    loadingProgress.visibility = View.INVISIBLE
                }
            }
            else{
                    if(userPassword.text.toString() != userConfermaPassword.text.toString()){
                        showMessage("Le due password inserite non coincidono")
                        registrati.visibility = View.VISIBLE
                        loadingProgress.visibility = View.INVISIBLE
                    }
                    else {
                        showMessage("Ricontrolla di aver inserito tutti i campi")
                        registrati.visibility = View.VISIBLE
                        loadingProgress.visibility = View.INVISIBLE
                    }
            }
        })

        tv_login.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun CreateUserAccount(user: HashMap<String, String>) {
        auth.createUserWithEmailAndPassword(user["Email"].toString(), user["Password"].toString()).addOnCompleteListener{ task ->
            if(task.isSuccessful){
                showMessage("Account creato con successo")
            }
        }
    }

    private fun showMessage(messaggio: String) {
        Toast.makeText(this, messaggio, Toast.LENGTH_LONG).show()
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
                imgProfilePhoto.setImageURI(selectedImage)

            }
        }

}

