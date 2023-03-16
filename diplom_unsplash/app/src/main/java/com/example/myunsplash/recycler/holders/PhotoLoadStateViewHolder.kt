package com.example.myunsplash.recycler.holders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.myunsplash.databinding.LoadStatePhotoBinding

class PhotoLoadStateViewHolder(
    private val binding: LoadStatePhotoBinding,
    private val retry: () -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.loadStatePhotoImageViewRetry.setOnClickListener { retry() }
    }

    fun bind(loadState: LoadState) {
        binding.loadStatePhotoProgressBar.isVisible = loadState is LoadState.Loading
        binding.loadStatePhotoImageViewRetry.isVisible = loadState is LoadState.Error
        binding.loadStatePhotoTextViewErrorMassage.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun getInstance(
            parent: ViewGroup,
            retry: () -> Unit,
        ): PhotoLoadStateViewHolder {
            val binding = LoadStatePhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return PhotoLoadStateViewHolder(binding, retry)
        }
    }
}