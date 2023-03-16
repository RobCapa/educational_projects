package com.example.myunsplash.recycler.adapters

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.myunsplash.data.LikeInfo
import com.example.myunsplash.data.Photo
import com.example.myunsplash.recycler.holders.PhotoHolder

class PhotosRecyclerAdapter(
    private val onClickOnLike: (String, Boolean, Int) -> Unit,
    private val onClickOnItem: (Int, View) -> Unit,
) : PagingDataAdapter<Photo, PhotoHolder>(diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        return PhotoHolder.getInstance(parent, onClickOnItem, onClickOnLike)
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun updateLikeStatusForPhoto(likeInfo: LikeInfo, position: Int) {
        getItem(position)?.let { item ->
            item.likedByUser = likeInfo.likedByUser
            item.likes = likeInfo.likesNumber
            notifyItemChanged(position)
        }
    }

    companion object {
        private val diff = object : DiffUtil.ItemCallback<Photo>() {
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo) =
                oldItem == newItem
        }
    }
}