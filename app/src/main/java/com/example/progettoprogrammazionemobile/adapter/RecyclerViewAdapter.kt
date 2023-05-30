package com.example.progettoprogrammazionemobile.adapter

import android.content.Intent
import android.util.EventLog.Event
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.progettoprogrammazionemobile.R
import com.example.progettoprogrammazionemobile.activity.EventDetailsActivity
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

        holder.dettagli.setOnClickListener{ v ->
            val eventDetails = Intent(v.context, EventDetailsActivity::class.java)
            val dettagli = bundleOf(
                "titolo" to event.titolo,
                "data" to event.data,
                "ora" to event.ora,
                "luogo" to event.luogo,
                "ruoli_richiesti" to event.ruoli_richiesti,
                "persone_richieste" to event.persone_richieste,
                "descrizione" to event.descrizione,
                "creatore" to event.creatore
            )
            Log.d("Adapter", dettagli.toString())
            eventDetails.putExtras(dettagli)
            v.context.startActivity(eventDetails)
        }
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
        val dettagli : Button = itemView.findViewById(R.id.dettagli_btn)


    }

}