package com.example.onlinestore.presentation.fragments.product_page

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.domain.models.Product
import com.example.onlinestore.R
import com.example.onlinestore.app.OnlineStoreApp
import com.example.onlinestore.databinding.FragProductPageBinding
import com.example.onlinestore.databinding.ViewSpecificationBinding
import com.example.onlinestore.presentation.utils.CorrectFormWordHelper
import com.example.onlinestore.presentation.utils.HardcodeImageList
import com.example.onlinestore.presentation.utils.ImageSlideAdapter
import com.example.onlinestore.presentation.utils.ViewModelFactory
import javax.inject.Inject

class ProductPageFragment : Fragment(R.layout.frag_product_page) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<ProductPageViewModel>

    private val binding: FragProductPageBinding by viewBinding()
    private val viewModel: ProductPageViewModel by viewModels { viewModelFactory }

    private val args: ProductPageFragmentArgs by navArgs()

    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as OnlineStoreApp).appComponent.inject(this)

        product = savedInstanceState?.getParcelable(PRODUCT_EXTRA) ?: args.product
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        bindProduct()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(PRODUCT_EXTRA, product)
    }

    private fun bindProduct() {
        bindBasic()
        bindPrice()
        bindFeedback()
        bindProductInfo()
        bindLike()

        with(binding) {
            linkExpandedCollapseWithTextView(
                expandCollapseTextView = fragProductPageTextViewDescriptionExpandCollapse,
                collapsibleTextView = fragProductPageTextViewDescription,
            )

            linkExpandedCollapseWithTextView(
                expandCollapseTextView = fragProductPageTextViewCompositionExpandCollapse,
                collapsibleTextView = fragProductPageTextViewComposition,
            )
        }
    }

    private fun linkExpandedCollapseWithTextView(
        expandCollapseTextView: TextView,
        collapsibleTextView: TextView
    ) {
        expandCollapseTextView.setOnClickListener {
            if (collapsibleTextView.maxLines == Int.MAX_VALUE) {
                changeMaxLinesOfTextView(collapsibleTextView, 2)
                expandCollapseTextView.text =
                    getString(R.string.fragProductPage_textView_expandTextView)
            } else {
                changeMaxLinesOfTextView(collapsibleTextView, Int.MAX_VALUE)
                expandCollapseTextView.text =
                    getString(R.string.fragProductPage_textView_collapseTextView)
            }
        }
    }

    private fun bindBasic() {
        with(binding) {
            with(product) {
                val productCounterPattern =
                    when (CorrectFormWordHelper.identifyCase(available.toInt())) {
                        CorrectFormWordHelper.Case.FIRST -> R.string.fragProductPage_pattern1_productCounter
                        CorrectFormWordHelper.Case.SECOND -> R.string.fragProductPage_pattern2_productCounter
                        CorrectFormWordHelper.Case.THIRD -> R.string.fragProductPage_pattern3_productCounter
                    }

                fragProductPageTextViewTitle.text = title
                fragProductPageTextViewBrand.text = title
                fragProductPageTextViewSubtitle.text = subtitle
                fragProductPageTextViewDescription.text = description
                fragProductPageTextViewComposition.text = ingredients
                fragProductPageTextViewProductCounter.text =
                    getString(productCounterPattern).format(available)

                fragProductPageViewPagerGallery.adapter = ImageSlideAdapter(
                    context = requireContext(),
                    imageIds = HardcodeImageList.getProductImages(id),
                )
                fragProductPageTabLayoutDots.setupWithViewPager(fragProductPageViewPagerGallery)
            }
        }
    }

    private fun bindPrice() {
        with(binding) {
            with(product.price) {
                fragProductPageTextViewCurrentPrice.text = "$priceWithDiscount $unit"
                fragProductPageTextViewOldPrice.text = "$price $unit"
                fragProductPageTextViewDiscount.text =
                    getString(R.string.pattern_discount).format(discount)

                fragProductPageTextViewAddToCartCurrentPrice.text = "$priceWithDiscount$unit"
                fragProductPageTextViewAddToCartOldPrice.text = "$price$unit"

            }
        }
    }

    private fun bindFeedback() {
        with(binding) {
            with(product.feedback) {
                val ratingCounterPattern =
                    when (CorrectFormWordHelper.identifyCase(count)) {
                        CorrectFormWordHelper.Case.FIRST -> R.string.fragProductPage_pattern1_ratingCounter
                        CorrectFormWordHelper.Case.SECOND -> R.string.fragProductPage_pattern2_ratingCounter
                        CorrectFormWordHelper.Case.THIRD -> R.string.fragProductPage_pattern3_ratingCounter
                    }

                fragProductPageMaterialRatingBar.rating = rating
                fragProductPageTextViewRatingAndFeedbackCounter.text =
                    getString(ratingCounterPattern).format(rating, count)
            }
        }
    }

    private fun bindProductInfo() {
        product.info.forEach { info ->
            ViewSpecificationBinding
                .inflate(layoutInflater)
                .apply {
                    viewSpecificationTextViewTitle.text = info.title
                    viewSpecificationTextViewValue.text = info.value
                }
                .root
                .let { view ->
                    binding.fragProductPageLinearLayoutSpecifications.addView(view)
                }
        }
    }

    private fun bindLike() {
        with(binding.fragProductPageImageViewLike) {
            val likeImage =
                if (product.isFavorite) R.drawable.ic_heart
                else R.drawable.ic_heart_hollow

            setOnClickListener { likeProduct() }
            setImageResource(likeImage)
        }
    }

    private fun changeMaxLinesOfTextView(textView: TextView, maxLines: Int) {
        with(textView) {
            val initialHeight = height

            textView.maxLines = maxLines

            measure(
                View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            )
            val targetHeight = measuredHeight

            val anim = ValueAnimator.ofInt(initialHeight, targetHeight)
            anim.addUpdateListener { valueAnimator ->
                val value = valueAnimator.animatedValue as Int
                val layoutParams = textView.layoutParams

                layoutParams.height = value
                textView.layoutParams = layoutParams
            }

            anim.duration = 300
            anim.start()
        }
    }

    private fun likeProduct() {
        if (product.isFavorite) viewModel.removeProductFromFavorites(product)
        else viewModel.addProductToFavorites(product)

        product = product.copy(isFavorite = !product.isFavorite)
        bindLike()
    }

    private fun setOnClickListeners() {
        binding.fragProductPageTopAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        private const val PRODUCT_EXTRA = "product"
    }
}