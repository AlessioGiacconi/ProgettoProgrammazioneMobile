package com.example.progettoprogrammazionemobile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.progettoprogrammazionemobile.R
import com.example.progettoprogrammazionemobile.data_class.EventDataClass


class RecyclerViewAdapter(private val eventList : ArrayList<EventDataClass>) : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewAdapter.MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.event_card_item, parent, false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.MyViewHolder, position: Int) {

        val event : EventDataClass = eventList[position]
        holder.titolo.text = event.titolo
        holder.data.text = event.data
        holder.ora.text = event.ora
        holder.luogo.text = event.luogo
        holder.nGiocatori.text = event.persone_richieste.toString()
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    public class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val titolo : TextView = itemView.findViewById(R.id.card_titolo)
        val data : TextView = itemView.findViewById(R.id.card_data)
        val ora : TextView = itemView.findViewById(R.id.card_ora)
        val luogo : TextView = itemView.findViewById(R.id.card_luogo)
        val nGiocatori : TextView = itemView.findViewById(R.id.card_numero_giocatori)


    }

}