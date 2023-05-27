package com.example.progettoprogrammazionemobile.data_class

import com.google.type.DateTime

data class EventDataClass (
    val titolo: String? = "",
    val data : String? = "",
    val ora : String? = "",
    val luogo : String? = "",
    val ruoli_richiesti: List<String>? = null,
    val persone_richieste: Long? = 0,
    val descrizione: String? = "",
    val creatore: String? = ""
)

