package com.example.progettoprogrammazionemobile.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.progettoprogrammazionemobile.MainActivity
import com.example.progettoprogrammazionemobile.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val tv_register = findViewById<TextView>(R.id.registrati)
        val login_btn = findViewById<Button>(R.id.login_btn)

        val email = findViewById<EditText>(R.id.email)
        val password = findViewById<EditText>(R.id.password)

        login_btn.setOnClickListener{
            if(email.text.toString().isEmpty() || password.text.toString().isEmpty()){
                Toast.makeText(this, "Inserire le credenziali", Toast.LENGTH_SHORT).show()
            }else{
                auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener(this){ task ->
                    if (task.isSuccessful){
                        Log.d("LoginActivity", "accesso  eseguito")
                        updateUI()
                    } else {
                        Log.w("LoginActivity", "accesso fallito", task.exception)
                        Toast.makeText(this, "Autenticazione fallita, riprova", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }


        tv_register.setOnClickListener {
            startActivity(Intent(this, CreateEventActivity::class.java))
        }
    }

    private fun updateUI() {
        val mainActivity = Intent(this, MainActivity::class.java)
        startActivity(mainActivity)
        finish()
    }
}