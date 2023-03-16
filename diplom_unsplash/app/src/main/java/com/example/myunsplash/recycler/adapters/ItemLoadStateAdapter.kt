package com.example.myunsplash.recycler.adapters

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.myunsplash.recycler.holders.PhotoLoadStateViewHolder

class ItemLoadStateAdapter(
    private val retry: () -> Unit,
) : LoadStateAdapter<PhotoLoadStateViewHolder>() {

    override fun onBindViewHolder(holder: PhotoLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
        (holder.itemView.layoutParams as? StaggeredGridLayoutManager.LayoutParams)
            ?.isFullSpan = true
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PhotoLoadStateViewHolder {
        return PhotoLoadStateViewHolder.getInstance(parent, retry)
    }
}