package com.example.myunsplash.recycler.adapters

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.myunsplash.data.LikeInfo
import com.example.myunsplash.data.Photo
import com.example.myunsplash.data.UnsplashCollection
import com.example.myunsplash.interfaces.Equatable
import com.example.myunsplash.recycler.holders.CollectionHolder
import com.example.myunsplash.recycler.holders.HolderAbstract
import com.example.myunsplash.recycler.holders.PhotoHolder

class ProfileRecyclerAdapter(
    private val onClickOnItem: (Int, View) -> Unit,
    private val onClickOnLike: (String, Boolean, Int) -> Unit,
) : PagingDataAdapter<Equatable, HolderAbstract>(diff) {

    override fun onBindViewHolder(holder: HolderAbstract, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderAbstract {
        return when (viewType) {
            PHOTO -> {
                PhotoHolder.getInstance(
                    parent,
                    onClickOnItem,
                    onClickOnLike,
                )
            }
            COLLECTION -> {
                CollectionHolder.getInstance(
                    parent,
                    onClickOnItem,
                )
            }
            else -> error("ViewType is incorrect")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (snapshot().items[position]) {
            is Photo -> PHOTO
            is UnsplashCollection -> COLLECTION
            else -> -1
        }
    }

    fun updateLikeStatusForPhoto(likeInfo: LikeInfo, position: Int) {
        (getItem(position) as? Photo)?.let { item ->
            item.likedByUser = likeInfo.likedByUser
            item.likes = likeInfo.likesNumber
            notifyItemChanged(position)
        }
    }

    companion object {
        const val PHOTO = 0
        const val COLLECTION = 1

        val diff = object : DiffUtil.ItemCallback<Equatable>() {
            override fun areItemsTheSame(oldItem: Equatable, newItem: Equatable): Boolean {
                return when {
                    oldItem is Photo && newItem is Photo -> {
                        oldItem.id == newItem.id
                    }
                    oldItem is UnsplashCollection && newItem is UnsplashCollection -> {
                        oldItem.id == newItem.id
                    }
                    else -> false
                }
            }

            override fun areContentsTheSame(oldItem: Equatable, newItem: Equatable): Boolean {
                return when {
                    oldItem is Photo && newItem is Photo -> {
                        oldItem == newItem
                    }
                    oldItem is UnsplashCollection && newItem is UnsplashCollection -> {
                        oldItem == newItem
                    }
                    else -> false
                }
            }
        }
    }
}