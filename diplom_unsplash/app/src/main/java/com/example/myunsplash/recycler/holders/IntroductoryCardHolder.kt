package com.example.myunsplash.recycler.holders

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.myunsplash.data.IntroCard
import com.example.myunsplash.databinding.ItemIntroductoryCardBinding

class IntroductoryCardHolder(
    private val binding: ItemIntroductoryCardBinding
) : RecyclerView.ViewHolder(binding.root) {
    private val context: Context
        get() = binding.root.context

    fun bind(item: IntroCard) {
        binding.itemIntroCardTextView.text = context.getString(item.text)
        binding.itemIntroCardImageView.setImageDrawable(context.getDrawable(item.image))
    }
}