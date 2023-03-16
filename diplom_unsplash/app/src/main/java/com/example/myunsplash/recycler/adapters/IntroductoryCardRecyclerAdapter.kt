package com.example.myunsplash.recycler.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myunsplash.data.IntroCard
import com.example.myunsplash.databinding.ItemIntroductoryCardBinding
import com.example.myunsplash.recycler.holders.IntroductoryCardHolder

class IntroductoryCardRecyclerAdapter(
    private val listItems: List<IntroCard>
) : RecyclerView.Adapter<IntroductoryCardHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroductoryCardHolder {
        val binding = ItemIntroductoryCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IntroductoryCardHolder(binding)
    }

    override fun onBindViewHolder(holder: IntroductoryCardHolder, position: Int) {
        holder.bind(listItems[position])
    }

    override fun getItemCount(): Int = listItems.size
}