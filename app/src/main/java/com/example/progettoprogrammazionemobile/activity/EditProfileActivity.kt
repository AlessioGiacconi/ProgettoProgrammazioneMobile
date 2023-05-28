package com.example.progettoprogrammazionemobile.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.progettoprogrammazionemobile.MainActivity
import com.example.progettoprogrammazionemobile.R
import com.example.progettoprogrammazionemobile.data_class.UserDataClass
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.FileNotFoundException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditProfileActivity : AppCompatActivity() {

    lateinit var dateEdit: EditText
    private var cal = Calendar.getInstance()
    var newImage: Uri? = null
    lateinit var editProfileImage: ImageView
    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    private lateinit var storageRef: StorageReference
    private lateinit var loggedUser: UserDataClass


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        auth = Firebase.auth
        storageRef = FirebaseStorage.getInstance().reference.child("profileImages")

        val sesso = listOf("Maschio", "Femmina")
        val ruolo = listOf("Attaccante", "Difensore", "Portiere")
        val autoCompleteSesso: AutoCompleteTextView = findViewById(R.id.Autocomplete_edit_sesso)
        val adapterSesso = ArrayAdapter(this, R.layout.dropdown_item, sesso)
        val autoCompleteRuolo: AutoCompleteTextView = findViewById(R.id.Autocomplete_edit_ruolo)
        val adapterRuolo = ArrayAdapter(this, R.layout.dropdown_item, ruolo)
        autoCompleteSesso.setAdapter(adapterSesso)
        autoCompleteRuolo.setAdapter(adapterRuolo)

        editProfileImage = findViewById(R.id.imageView)
        val editNome = findViewById<EditText>(R.id.et_edit_nome)
        val editCognome = findViewById<EditText>(R.id.et_edit_cognome)
        val editTelefono = findViewById<EditText>(R.id.et_edit_telefono)
        dateEdit = findViewById(R.id.et_edit_data_di_nascita)
        val editEmail = findViewById<EditText>(R.id.et_edit_email)
        val editPassword = findViewById<EditText>(R.id.et_edit_password)
        val editConfermaPassword = findViewById<EditText>(R.id.et_edit_conferma_password)



        autoCompleteSesso.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, _, i, _ ->

                var editSesso = adapterView.getItemAtPosition(i) as String
            }

        autoCompleteRuolo.onItemClickListener =
            AdapterView.OnItemClickListener { adapterView, view, i, l ->

               var editRuolo = adapterView.getItemAtPosition(i) as String
            }

        val confrim_btn = findViewById<ExtendedFloatingActionButton>(R.id.confirm_btn)


        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }

        dateEdit.setOnClickListener(View.OnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        })

        if (auth.currentUser != null) {
            db.collection("users").document(auth.currentUser!!.email.toString()).get()
                .addOnSuccessListener { doc ->
                    loggedUser = UserDataClass(
                        doc["Nome"].toString(),
                        doc["Cognome"].toString(),
                        doc["Telefono"].toString(),
                        doc["Data di Nascita"].toString(),
                        doc["Sesso"].toString(),
                        doc["Ruolo"].toString(),
                        doc["Email"].toString(),
                        doc["Password"].toString(),
                        doc["Immagine Profilo"].toString()
                    )

                    editNome.setText(loggedUser.nome)
                    editCognome.setText(loggedUser.cognome)
                    editTelefono.setText(loggedUser.telefono)
                    dateEdit.setText(loggedUser.data_di_nascita)
                    autoCompleteSesso.setText(loggedUser.sesso)
                    autoCompleteRuolo.setText(loggedUser.ruolo)
                    editEmail.setText(loggedUser.email)
                    editPassword.setText(loggedUser.password)
                    editConfermaPassword.setText(loggedUser.password)
                    if (loggedUser.profile_img != "") {
                        Glide.with(this).load(loggedUser.profile_img).into(editProfileImage)
                    } else {
                        editProfileImage.setImageResource(R.drawable.icona_profilo)
                        Log.d("ProfileActivity", editProfileImage.toString())
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Errore di comunicazione col database", Toast.LENGTH_SHORT)
                        .show()
                }
        }

        editProfileImage.setOnClickListener(View.OnClickListener {
            openGallery()
        })
// fixare i check sulle nuove password e fixxare l'image view e non cambia il nome della foto nello storage
        confrim_btn.setOnClickListener(View.OnClickListener {
            if (editPassword.text.toString() == editConfermaPassword.text.toString() && editPassword.text.toString().length >= 8) {

                if (editEmail.text.toString() != loggedUser.email) {
                    auth.currentUser!!.updateEmail(editEmail.text.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("EditActivity", "Email updated")
                            } else {
                                Log.d("EditActivity", "Failed to update email")
                            }
                        }
                }

                if (editPassword.text.toString() != loggedUser.password && editPassword.text.toString().length >= 8) {
                    auth.currentUser!!.updatePassword(editPassword.text.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("EditActivity", "Password updated")
                            } else {
                                Log.d("EditActivity", "Failed to update password")
                            }

                        }
                } else {
                    if (editPassword.text.toString().length < 8) {
                        showMessage("Password deve contenere almeno 8 caratteri")
                    } else {
                        if (editPassword.text.toString() != editConfermaPassword.text.toString())
                            showMessage("Le due password non coincidono, riprova")
                    }
                }

                val editedUser = hashMapOf(
                    "Nome" to editNome.text.toString(),
                    "Cognome" to editCognome.text.toString(),
                    "Telefono" to editTelefono.text.toString(),
                    "Data di Nascita" to dateEdit.text.toString(),
                    "Sesso" to autoCompleteSesso.text.toString(),
                    "Ruolo" to autoCompleteRuolo.text.toString(),
                    "Email" to editEmail.text.toString(),
                    "Password" to editPassword.text.toString(),
                )
                Log.d("EditActivity", "Edit hash map:$editedUser")

                saveEditedInfo(editedUser)
            }
        })

    }

    private fun saveEditedInfo(editedUser: HashMap<String, String>) {
        Log.d("EditActivity", "Sono qui dentro")
        Log.d("EditActivity", newImage.toString())
        if (newImage == null) {
            editedUser["Immagine Profilo"] = loggedUser.profile_img
            db.collection("users").document(loggedUser.email).delete()
            db.collection("users").document(editedUser["Email"].toString()).set(editedUser)
                .addOnSuccessListener {
                    showMessage("Informazioni modificate con successo")
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }.addOnFailureListener {
                showMessage("Qualcosa è andato storto durante la modifica")
            }

        } else {
            val deleteImg = storageRef.child("profileImages/${loggedUser.email}")
            deleteImg.delete()
            val email = editedUser["Email"].toString()
            val storageReference =
                FirebaseStorage.getInstance().getReference("/profileImages/$email")

            storageReference.putFile(newImage!!).addOnSuccessListener { it ->
                storageReference.downloadUrl.addOnSuccessListener { it ->
                    it.toString()
                    editedUser["Immagine Profilo"] = it.toString()
                    db.collection("users").document(loggedUser.email).delete()
                    db.collection("users").document(editedUser["Email"].toString()).set(editedUser)
                        .addOnSuccessListener {
                            showMessage("Informazioni modificate con successo")
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }.addOnFailureListener {
                        showMessage("Qualcosa è andato storto durante la modifica")
                    }

                }
            }
        }

    }


    private fun updateDateInView() {
        val myFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.ITALY)
        dateEdit.setText(sdf.format(cal.time))
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
            Log.d("EditActivity", "Photo was selected")

            val data = result.data
            val selectedImage: Uri? = data?.data
            var imageStream: InputStream? = null
            try {
                imageStream = contentResolver.openInputStream(selectedImage!!)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            BitmapFactory.decodeStream(imageStream)
            newImage = data?.data
            Log.d("EditActivity", newImage.toString())
            editProfileImage.setImageURI(selectedImage)

        }
    }

    private fun showMessage(messaggio: String) {
        Toast.makeText(this, messaggio, Toast.LENGTH_LONG).show()
    }

}