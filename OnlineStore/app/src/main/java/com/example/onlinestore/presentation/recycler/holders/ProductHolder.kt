package com.example.onlinestore.presentation.recycler.holders

import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.Product
import com.example.onlinestore.R
import com.example.onlinestore.databinding.ItemProductBinding
import com.example.onlinestore.presentation.utils.HardcodeImageList
import com.example.onlinestore.presentation.utils.ImageSlideAdapter
import com.example.onlinestore.presentation.utils.ViewPagerOnTouchListener


class ProductHolder(
    private val binding: ItemProductBinding,
    private val onClickOnItem: (Product) -> Unit,
    private val onClickOnLikeCallback: (Product) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(product: Product) {
        val context = binding.root.context

        with(binding) {
            with(product.price) {
                itemProductTextViewOldPrice.text = "$price $unit"
                itemProductTextViewCurrentPrice.text = "$priceWithDiscount $unit"
                itemProductTextViewDiscount.text =
                    context.getString(R.string.pattern_discount).format(discount)
            }

            with(product) {
                itemProductTextViewTitle.text = title
                itemProductTextViewSubtitle.text = subtitle

                val likeImage = if (isFavorite) R.drawable.ic_heart else R.drawable.ic_heart_hollow
                fragItemProductImageViewLike.setOnClickListener { onClickOnLikeCallback(product) }
                fragItemProductImageViewLike.setImageResource(likeImage)
            }

            with(product.feedback) {
                itemProductTextViewRating.text = "$rating"
                itemProductTextViewFeedbackCount.text = "($count)"
            }

            fragItemProductViewPagerGallery.setOnTouchListener(
                ViewPagerOnTouchListener { onClickOnItem(product) }
            )

            root.setOnClickListener { onClickOnItem(product) }

            fragItemProductViewPagerGallery.adapter = ImageSlideAdapter(
                context = binding.root.context,
                imageIds = HardcodeImageList.getProductImages(product.id),
            )
            fragItemProductTabLayoutDots.setupWithViewPager(fragItemProductViewPagerGallery)
        }
    }
}