package com.example.progettoprogrammazionemobile.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettoprogrammazionemobile.R
import com.example.progettoprogrammazionemobile.adapter.RecyclerViewAdapter
import com.example.progettoprogrammazionemobile.data_class.EventDataClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase

class CreatedEventsFragment : Fragment() {

    private lateinit var fragmentView: View
    private lateinit var createdEventRV: RecyclerView
    private lateinit var createdEventsArrayList: ArrayList<EventDataClass>
    private lateinit var cardAdapter: RecyclerViewAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_created_events, container, false)

        auth = Firebase.auth

        createdEventRV = fragmentView.findViewById(R.id.recyclerViewCreatedEvents)
        createdEventRV.layoutManager = LinearLayoutManager(context)
        createdEventRV.setHasFixedSize(true)

        createdEventsArrayList = arrayListOf()

        cardAdapter = RecyclerViewAdapter(createdEventsArrayList)

        createdEventRV.adapter = cardAdapter

        eventChangeListener()

        return fragmentView
    }

    private fun eventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("events").addSnapshotListener(object: EventListener<QuerySnapshot>{
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?){
                if(error != null){
                    Log.e("Firestore Error", error.message.toString())
                    return
                }
                for(dc : DocumentChange in value?.documentChanges!!){
                    if(dc.type == DocumentChange.Type.ADDED){
                        val createdEvent = dc.document.toObject(EventDataClass::class.java)
                        Log.d("Fragment", "Event:$createdEvent")
                        if(createdEvent.creatore == auth.currentUser!!.email){
                            createdEventsArrayList.add(createdEvent)
                        }
                    }
                }
                cardAdapter.notifyDataSetChanged()
            }
        })

    }

}