package com.example.progettoprogrammazionemobile.data_class

data class UserDataClass(
    val nome: String,
    val cognome: String,
    val telefono: String,
    val data_di_nascita: String,
    val sesso: String,
    val ruolo: String,
    val email: String,
    val password: String,
    val profile_img: String,
    val miei_eventi: List<String>? = null
)
