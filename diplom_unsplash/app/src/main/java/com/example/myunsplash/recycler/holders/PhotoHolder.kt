package com.example.myunsplash.recycler.holders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.example.myunsplash.R
import com.example.myunsplash.data.Photo
import com.example.myunsplash.databinding.ItemPhotoBinding
import com.example.myunsplash.utils.getIntFromTheme

class PhotoHolder(
    binding: ItemPhotoBinding,
    private val onClickOnLike: (String, Boolean, Int) -> Unit,
    private val onClickOnItem: (Int, View) -> Unit,
) : HolderAbstract(binding) {

    private var currentPhotoId = ""

    override fun bind(item: Any) {
        item as? Photo ?: error("Invalid type of item")
        binding as ItemPhotoBinding

        if (currentPhotoId == item.id) {
            bindLike(item)
            return
        }

        hideView()
        ViewCompat.setTransitionName(binding.itemPhotoImageViewPhoto, "photo_${item.id}")
        currentPhotoId = item.id

        Glide.with(binding.root)
            .load(item.url)
            .listener(requestListenerShowView)
            .into(binding.itemPhotoImageViewPhoto)

        with(binding.itemPhotoIncludeInformationPlate) {
            informationPlateTextViewName.text = item.author.name
            informationPlateTextViewUserName.text = "@${item.author.username}"
            Glide.with(binding.root)
                .load(item.author.avatarUrl)
                .into(informationPlateImageViewAuthorAvatar)
        }

        bindLike(item)

        binding.root.setOnClickListener() {
            onClickOnItem(absoluteAdapterPosition, binding.itemPhotoImageViewPhoto)
        }
    }

    private fun bindLike(photo: Photo) {
        binding as ItemPhotoBinding

        val likeDrawable = context.getDrawable(
            if (photo.likedByUser) R.drawable.ic_baseline_like
            else R.drawable.ic_baseline_like_border
        )
        val likeColor =
            if (photo.likedByUser) context.getColor(R.color.red)
            else context.getIntFromTheme(R.attr.mainTextColor)

        with(binding.itemPhotoIncludeInformationPlate) {
            informationPlateTextViewLikesNumber.text = "${photo.likes}"
            informationPlateImageViewLike.apply {
                setImageDrawable(likeDrawable)
                setColorFilter(likeColor)
                setOnClickListener {
                    onClickOnLike(
                        photo.id,
                        !photo.likedByUser,
                        absoluteAdapterPosition
                    )
                }
            }
        }
    }

    companion object {
        fun getInstance(
            parent: ViewGroup,
            onClickOnItem: (Int, View) -> Unit,
            onClickOnLike: (String, Boolean, Int) -> Unit,
        ): PhotoHolder {
            val binding = ItemPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return PhotoHolder(
                binding,
                onClickOnLike,
                onClickOnItem
            )
        }
    }
}