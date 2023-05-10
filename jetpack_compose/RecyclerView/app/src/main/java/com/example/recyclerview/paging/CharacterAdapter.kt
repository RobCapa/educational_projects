package com.example.recyclerview.paging

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview.R
import com.example.recyclerview.api.Character

class CharacterAdapter : PagedListAdapter<Character, ViewHolder>(DiffUtilCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.view_character, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    fun bind(character: Character) {
        (itemView as? ViewGroup)?.findViewById<TextView>(R.id.name)?.text =
            "${character.name}  ${character.species}"
    }
}

class DiffUtilCallback : DiffUtil.ItemCallback<Character>() {
    override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean =
        oldItem.name == newItem.name && oldItem.species == newItem.species
}