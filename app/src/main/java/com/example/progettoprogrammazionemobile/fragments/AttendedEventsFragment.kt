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
import java.util.ArrayList


class AttendedEventsFragment : Fragment() {

    private lateinit var fragmentView: View
    private lateinit var attendedEventRV: RecyclerView
    private lateinit var attendedEventsArrayList: ArrayList<EventDataClass>
    private lateinit var cardAdapter: RecyclerViewAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var userEventList: ArrayList<*>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_attended_events, container, false)

        auth = Firebase.auth

        attendedEventRV = fragmentView.findViewById(R.id.recyclerViewAttendedEvents)
        attendedEventRV.layoutManager = LinearLayoutManager(context)
        attendedEventRV.setHasFixedSize(true)

        attendedEventsArrayList = arrayListOf()

        cardAdapter = RecyclerViewAdapter(attendedEventsArrayList)

        attendedEventRV.adapter = cardAdapter

        eventChangeListener()

        return fragmentView
    }

    private fun eventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("events").addSnapshotListener((object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                if (error != null) {
                    Log.e("FireStore Error", error.message.toString())
                    return
                }
                db.collection("users").document(auth.currentUser!!.email.toString()).get()
                    .addOnSuccessListener { doc ->
                        userEventList = (doc["Miei Eventi"] as ArrayList<*>?)!!
                        Log.d("Fragment", userEventList.toString())
                        for (dc: DocumentChange in value?.documentChanges!!) {
                            if (dc.type == DocumentChange.Type.ADDED) {
                                val attendedEvent = dc.document.toObject(EventDataClass::class.java)
                                for (e in userEventList) {
                                    if (e == attendedEvent.titolo) {
                                        Log.d("Fragment", "sono qui")
                                        attendedEventsArrayList.add(attendedEvent)
                                    }
                                }
                            }
                        }
                        cardAdapter.notifyDataSetChanged()
                    }
            }
        }))
    }
}