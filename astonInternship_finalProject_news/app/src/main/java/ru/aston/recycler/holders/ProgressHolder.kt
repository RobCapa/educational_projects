package ru.aston.recycler.holders

import android.app.ActionBar.LayoutParams
import androidx.recyclerview.widget.RecyclerView
import ru.aston.databinding.ItemProgressBinding

class ProgressHolder(
    private val binding: ItemProgressBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(matchParent: Boolean) {
        binding.root
            .layoutParams
            .height = if (matchParent) LayoutParams.MATCH_PARENT else LayoutParams.WRAP_CONTENT
    }
}