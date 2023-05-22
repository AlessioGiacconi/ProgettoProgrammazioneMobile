package com.example.progettoprogrammazionemobile.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettoprogrammazionemobile.R
import com.example.progettoprogrammazionemobile.adapter.RecyclerViewAdapter
import com.example.progettoprogrammazionemobile.data_class.EventDataClass
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class SearchEventActivity : AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var eventArrayList : ArrayList<EventDataClass>
    private lateinit var cardAdapter : RecyclerViewAdapter
    private lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_event)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        eventArrayList = arrayListOf()

        cardAdapter = RecyclerViewAdapter(eventArrayList)

        recyclerView.adapter = cardAdapter

        eventChangeListener()

    }

    // funzione che si occupa del fetch dei dati provenienti dal db e verrano inseriti nella card
    private fun eventChangeListener() {

        db = FirebaseFirestore.getInstance()
        db.collection("events").addSnapshotListener(object: EventListener<QuerySnapshot>{

            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if(error != null){
                    Log.e("Firestore Error", error.message.toString())
                    return
                }
                for(dc : DocumentChange in value?.documentChanges!!){

                    if(dc.type == DocumentChange.Type.ADDED){
                        Log.d("SearchActivity", "sono qui")
                        Log.d("SearcActivity", dc.document.data.toString())
                        eventArrayList.add(dc.document.toObject(EventDataClass::class.java))
                    }
                }
                Log.d("SearchActivitu", "sono oltre")
                cardAdapter.notifyDataSetChanged()
            }

        })

    }
}