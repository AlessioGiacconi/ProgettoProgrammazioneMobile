package com.example.progettoprogrammazionemobile.data_class

import android.net.Uri

data class UserDataClass(
    val nome: String,
    val cognome: String,
    val telefono: String,
    val sesso: String,
    val età: Int,
    val email: String,
    val password: String,
    val profile_img: Uri
)
