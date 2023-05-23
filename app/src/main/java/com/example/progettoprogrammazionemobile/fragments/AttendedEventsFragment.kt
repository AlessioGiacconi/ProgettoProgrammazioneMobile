package com.example.progettoprogrammazionemobile.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.progettoprogrammazionemobile.R


class AttendedEventsFragment : Fragment() {

    private lateinit var view : View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_attended_events, container, false)
        return view
    }
}