package com.example.gcr.adapter

import android.service.autofill.OnClickAction
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.gcr.R
import com.example.gcr.data.models.Console

class ConsoleAdapter (
    private var consoles: List<Console>,
    private val onItemClick: (Console) -> Unit
): RecyclerView.Adapter<ConsoleAdapter.ConsoleViewHolder>() {
    class ConsoleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        private val console_name = itemView.findViewById<TextView>(R.id.Console)

        fun bind(console: Console,onItemClick: (Console) -> Unit) {
            console.console_name.let{ name ->
                console_name.text = name
            }

            itemView.setOnClickListener {

                onItemClick(console)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsoleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_console_tag, parent, false)
        return ConsoleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConsoleViewHolder, position: Int) {
        if(position < consoles.size){
            holder.bind(consoles[position], onItemClick)
        } else {
            Log.w("fuera de rango","$position")
        }
    }

    override fun getItemCount(): Int {
        return consoles.size
    }

    fun updateList(newList: List<Console>) {
        consoles = newList
        notifyDataSetChanged()
    }
}