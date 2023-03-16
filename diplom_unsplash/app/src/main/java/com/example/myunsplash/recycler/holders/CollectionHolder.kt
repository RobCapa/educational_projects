package com.example.myunsplash.recycler.holders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.myunsplash.R
import com.example.myunsplash.data.UnsplashCollection
import com.example.myunsplash.databinding.ItemCollectionBinding

class CollectionHolder(
    binding: ItemCollectionBinding,
    private val onClickOnItem: (Int, View) -> Unit,
) : HolderAbstract(binding) {

    override fun bind(item: Any) {
        item as UnsplashCollection
        binding as ItemCollectionBinding

        ViewCompat.setTransitionName(binding.itemCollectionImageViewPhoto, "collection_${item.id}")

        hideView()

        Glide.with(binding.root)
            .load(item.coverPhotoUrl)
            .listener(requestListenerShowView)
            .into(binding.itemCollectionImageViewPhoto)

        binding.root.setOnClickListener {
            onClickOnItem(absoluteAdapterPosition, binding.itemCollectionImageViewPhoto)
        }

        binding.itemCollectionTextViewPhotosNumber.text = context
            .getString(R.string.collectionHolder_photosNumber).format(item.totalPhotos)
        binding.itemCollectionTextViewTitle.text = item.title

        with(binding.itemCollectionIncludeInformationPlate) {
            informationPlateGroupLike.isVisible = false

            informationPlateTextViewName.text = item.author.name
            informationPlateTextViewUserName.text = "@${item.author.username}"
            Glide.with(binding.root)
                .load(item.author.avatarUrl)
                .into(informationPlateImageViewAuthorAvatar)
        }
    }

    companion object {
        fun getInstance(
            parent: ViewGroup,
            onClickOnItem: (Int, View) -> Unit,
        ): CollectionHolder {
            val binding = ItemCollectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return CollectionHolder(
                binding,
                onClickOnItem,
            )
        }
    }
}