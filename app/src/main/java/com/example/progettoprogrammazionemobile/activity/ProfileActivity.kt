package com.example.progettoprogrammazionemobile.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.progettoprogrammazionemobile.R
import com.example.progettoprogrammazionemobile.data_class.UserDataClass
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.w3c.dom.Text


class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val db = Firebase.firestore
    private lateinit var storageRef : StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = Firebase.auth
        storageRef = FirebaseStorage.getInstance().reference.child("profileImages")



        val profileImage = findViewById<ImageView>(R.id.imageView)
        val nome = findViewById<TextView>(R.id.nome_utente)
        val cognome = findViewById<TextView>(R.id.cognome_utente)
        val telefono = findViewById<TextView>(R.id.telefono_utente)
        val ruolo = findViewById<TextView>(R.id.ruolo_utente)
        val sesso = findViewById<TextView>(R.id.sesso_utente)
        val dataDiNascita = findViewById<TextView>(R.id.data_nascita_utente)
        val email = findViewById<TextView>(R.id.email_utente)

        var loggedUser : UserDataClass

        val fabEdit = findViewById<ExtendedFloatingActionButton>(R.id.edit_btn)

        if(auth.currentUser != null) {
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

                    nome.text = loggedUser.nome
                    cognome.text = loggedUser.cognome
                    telefono.text = loggedUser.telefono
                    dataDiNascita.text = loggedUser.data_di_nascita
                    sesso.text = loggedUser.sesso
                    ruolo.text = loggedUser.ruolo
                    email.text = loggedUser.email
                    if (loggedUser.profile_img != ""){
                        Glide.with(this).load(loggedUser.profile_img).into(profileImage)
                    }else{
                        profileImage.setImageResource(R.drawable.icona_profilo)
                        Log.d("ProfileActivity", profileImage.toString())
                    }
                }.addOnFailureListener {
                Toast.makeText(this, "Errore di comunicazione col database", Toast.LENGTH_SHORT).show()
            }
        }

        fabEdit.setOnClickListener(View.OnClickListener {
            Log.d("ProfileActivity", "cliccato")
            startActivity(Intent(this, EditProfileActivity::class.java))
        })
    }
}