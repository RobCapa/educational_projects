package com.example.recyclerview.diffutil

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.R

class ElementAdapter(
    var elements: List<Worker>
) : RecyclerView.Adapter<ElementAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.view_element, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(elements[position])
    }

    override fun getItemCount(): Int = elements.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(element: Worker) {
            (itemView as? ViewGroup)?.findViewById<TextView>(R.id.title)?.text = element.name
            (itemView as? ViewGroup)?.findViewById<TextView>(R.id.number)?.text = element.position
        }
    }
}
