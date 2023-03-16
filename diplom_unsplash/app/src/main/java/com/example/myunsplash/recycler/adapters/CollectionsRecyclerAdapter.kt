package com.example.myunsplash.recycler.adapters

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.myunsplash.data.UnsplashCollection
import com.example.myunsplash.recycler.holders.CollectionHolder

class CollectionsRecyclerAdapter(
    private val onClickOnItem: (Int, View) -> Unit,
) : PagingDataAdapter<UnsplashCollection, CollectionHolder>(diff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionHolder {
        return CollectionHolder.getInstance(parent, onClickOnItem)
    }

    override fun onBindViewHolder(holder: CollectionHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    companion object {
        private val diff = object : DiffUtil.ItemCallback<UnsplashCollection>() {
            override fun areItemsTheSame(oldItem: UnsplashCollection, newItem: UnsplashCollection) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: UnsplashCollection,
                newItem: UnsplashCollection
            ) =
                oldItem == newItem
        }
    }
}