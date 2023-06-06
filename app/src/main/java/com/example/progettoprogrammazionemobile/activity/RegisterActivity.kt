package com.example.progettoprogrammazionemobile.activity

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.progettoprogrammazionemobile.MainActivity
import com.example.progettoprogrammazionemobile.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.FileNotFoundException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class RegisterActivity : AppCompatActivity() {

    lateinit var imgProfilePhoto: ImageView
    var pickedImage: Uri? = null
    private val PReqCode = 1
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    private var cal = Calendar.getInstance()
    lateinit var userDataDiNascita: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth

        val sesso = listOf("Maschio", "Femmina")
        val ruolo = listOf("Attaccante", "Difensore", "Portiere")
        val autoCompleteSesso: AutoCompleteTextView =
            findViewById(R.id.Autocomplete_text_view_sesso)
        val adapterSesso = ArrayAdapter(this, R.layout.dropdown_item, sesso)
        val autoCompleteRuolo: AutoCompleteTextView =
            findViewById(R.id.Autocomplete_text_view_ruolo)
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
        userDataDiNascita = findViewById(R.id.et_età)
        var userSesso = "Maschio"
        var userRuolo = "Attaccante"
        val userEmail = findViewById<EditText>(R.id.et_email)
        val userPassword = findViewById<EditText>(R.id.et_password)
        val userConfermaPassword = findViewById<EditText>(R.id.et_Conferma_password)
        imgProfilePhoto = findViewById(R.id.photoImageView)

        autoCompleteSesso.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, _, i, _ ->

                userSesso = adapterView.getItemAtPosition(i) as String
            }

        autoCompleteRuolo.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, i, l ->

                userRuolo = adapterView.getItemAtPosition(i) as String
            }

        autoCompleteRuolo.setOnClickListener{
            autoCompleteRuolo.setText("")
        }

        autoCompleteSesso.setOnClickListener {
            autoCompleteSesso.setText("")
        }

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }

        userDataDiNascita.setOnClickListener(View.OnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        })

        imgProfilePhoto.setOnClickListener(View.OnClickListener {
            if (Build.VERSION.SDK_INT >= 22) {
                checkAndRequestForPermission()
            } else {
                openGallery()
            }
        })



        registrati.setOnClickListener(View.OnClickListener {
            registrati.visibility = View.INVISIBLE
            loadingProgress.visibility = View.VISIBLE

            if (userNome.text.toString().isNotEmpty() && userCognome.text.toString()
                    .isNotEmpty() && userTelefono.text.toString().isNotEmpty() &&
                userDataDiNascita.text.toString()
                    .isNotEmpty() && userSesso.isNotEmpty() && userRuolo.isNotEmpty() && userEmail.text.toString()
                    .isNotEmpty() &&
                userPassword.text.toString().isNotEmpty() && userConfermaPassword.text.toString()
                    .isNotEmpty()
            ) {
                if (userPassword.text.toString().length >= 8 && userPassword.text.toString() == userConfermaPassword.text.toString()) {
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
                    Log.d("RegisterActivity", "Email:" + userEmail.text.toString())
                    Log.d("RegisterActivity", "Password: " + userPassword.text.toString())
                    createUserAccount(user)
                    registrati.visibility = View.INVISIBLE
                    loadingProgress.visibility = View.VISIBLE
                } else {
                    if (userPassword.text.toString().length < 8) {
                        showMessage("Password deve contenere almeno 8 caratteri")
                        registrati.visibility = View.VISIBLE
                        loadingProgress.visibility = View.INVISIBLE
                    } else {
                        showMessage("Le due password non coincidono, riprova")
                        registrati.visibility = View.VISIBLE
                        loadingProgress.visibility = View.INVISIBLE
                    }
                }
            } else {
                showMessage("Ricontrolla di aver inserito tutti i campi")
                registrati.visibility = View.VISIBLE
                loadingProgress.visibility = View.INVISIBLE
            }
        })

        tv_login.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun createUserAccount(user: HashMap<String, String>) {
        auth.createUserWithEmailAndPassword(user["Email"].toString(), user["Password"].toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(
                        "RegisterActivity",
                        "Succesfully created user with uid: ${task.result.user?.uid}"
                    )
                    uploadImageToFirebaseStorage(user)
                }
            }
    }

    private fun uploadImageToFirebaseStorage(user: HashMap<String, String>) {
        Log.d("RegisterActivity", "Sono nella funzione, pickedImageUri: $pickedImage")
        if (pickedImage == null) {
            user["Immagine Profilo"] = ""
            Log.d("RegisterActivity", "User hash map:$user")
            saveUserToFirebaseDatabase(user)
        } else {
            val email = user["Email"].toString()
            val storageReference =
                FirebaseStorage.getInstance().getReference("/profileImages/$email")

            storageReference.putFile(pickedImage!!).addOnSuccessListener {
                Log.d("RegisterActivity", "Image succesfully in storage: ${it.metadata?.path}")

                storageReference.downloadUrl.addOnSuccessListener { it ->
                    it.toString()
                    Log.d("RegisterActivity", "File Location:$it")
                    user["Immagine Profilo"] = it.toString()
                    Log.d("RegisterActivity", "User hash map:$user")
                    saveUserToFirebaseDatabase(user)
                }
            }
        }
    }

     fun saveUserToFirebaseDatabase(user: HashMap<String, String>) {
        Log.d("RegisterActivity", "Sono qui")
        db.collection("users").document(user["Email"].toString()).set(user).addOnSuccessListener {
            Log.d("RegisterActivity", "Utente registrato sul db")
            showMessage("Registrazione completata")
            updateUI()
        }.addOnFailureListener {
            showMessage("Qualcosa è andato storto durante la registrazione")
            Log.d("RegisterActivity", "Qualcosa è andato storto")
        }
    }

    private fun updateUI() {
        val mainActivity = Intent(this, MainActivity::class.java)
        startActivity(mainActivity)
        finish()
    }

    private fun showMessage(messaggio: String) {
        Toast.makeText(this, messaggio, Toast.LENGTH_LONG).show()
    }

    private fun checkAndRequestForPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                showMessage("È necessario accettare i permessi per continuare")
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PReqCode
                )
            }
        } else {
            openGallery()
        }

    }

    private fun openGallery() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        pickPhotoActivityResultLauncher.launch(photoPickerIntent)
    }

    private val pickPhotoActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("RegisterActivity", "Photo was selected")

            val data = result.data
            val selectedImage: Uri? = data?.data
            var imageStream: InputStream? = null
            try {
                imageStream = contentResolver.openInputStream(selectedImage!!)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            BitmapFactory.decodeStream(imageStream)
            pickedImage = data?.data
            imgProfilePhoto.setImageURI(selectedImage)

        }
    }

    private fun updateDateInView() {
        val myFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.ITALY)
        userDataDiNascita.setText(sdf.format(cal.time))
    }
}

