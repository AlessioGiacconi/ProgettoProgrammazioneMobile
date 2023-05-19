package com.example.progettoprogrammazionemobile.data_class

import com.google.type.DateTime

data class EventDataClass (
    val data_ora : DateTime,
    val luogo : String,
    val ruoli_richiesti: List<String>,
    val persone_richieste: Int
)

