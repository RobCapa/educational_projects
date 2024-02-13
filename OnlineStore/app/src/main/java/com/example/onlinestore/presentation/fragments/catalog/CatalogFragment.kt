package com.example.onlinestore.presentation.fragments.catalog

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.domain.models.Filters
import com.example.domain.models.Product
import com.example.onlinestore.R
import com.example.onlinestore.app.OnlineStoreApp
import com.example.onlinestore.databinding.FragCatalogBinding
import com.example.onlinestore.presentation.recycler.adapters.ProductRecyclerAdapter
import com.example.onlinestore.presentation.recycler.decorators.GridSpacingItemDecoration
import com.example.onlinestore.presentation.utils.AdapterDataObserverAnyChanges
import com.example.onlinestore.presentation.utils.ViewModelFactory
import com.example.onlinestore.presentation.utils.dp
import com.google.android.material.chip.Chip
import javax.inject.Inject

class CatalogFragment : Fragment(R.layout.frag_catalog) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<CatalogViewModel>

    private val binding: FragCatalogBinding by viewBinding()
    private val viewModel: CatalogViewModel by viewModels { viewModelFactory }

    private val recyclerAdapter = ProductRecyclerAdapter(
        ::navToProductPage,
        ::likeProduct,
    )
    private val dataObserverAnyChanges = AdapterDataObserverAnyChanges {
        binding.fragCatalogRecyclerViewProducts.scrollToPosition(0)
    }

    private var filters: Filters = Filters()
        set(newValue) {
            field = newValue
            onFiltersWasChanged()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as OnlineStoreApp).appComponent.inject(this)

        savedInstanceState?.getParcelable<Filters>(FILTERS_EXTRA)?.let {
            filters = it
        }

        recyclerAdapter.registerAdapterDataObserver(dataObserverAnyChanges)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeToViewModelResults()
        configureRecyclerView()
        configureSpinnerSortingStrategy()
        configureTagChips()

        viewModel.getAllProducts(filters)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(FILTERS_EXTRA, filters)
    }

    override fun onDestroy() {
        super.onDestroy()
        recyclerAdapter.unregisterAdapterDataObserver(dataObserverAnyChanges)
    }

    private fun configureTagChips() {
        Filters.ProductTag.entries.forEach { tag ->
            Chip(
                context,
                null,
                R.attr.tagChipStyle
            ).apply {
                text = getProductTagTitle(tag)
                isCheckable = true

                val currentTagIsChecked = this@CatalogFragment.filters.tags.contains(tag)
                isChecked = currentTagIsChecked
                isCloseIconVisible = currentTagIsChecked

                setOnCheckedChangeListener { _, isChecked ->
                    isCloseIconVisible = isChecked

                    if (isChecked) {
                        this@CatalogFragment.filters = this@CatalogFragment.filters.copy(
                            tags = listOf(tag)
                        )
                    }
                }

                setOnCloseIconClickListener {
                    isChecked = false
                    isCloseIconVisible = false
                }

            }.let(binding.fragCatalogChipGroupTags::addView)
        }

        binding.fragCatalogChipGroupTags.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isEmpty()) {
                this@CatalogFragment.filters = this@CatalogFragment.filters.copy(
                    tags = listOf(Filters.ProductTag.CATALOG)
                )
            }
        }
    }

    private fun getProductTagTitle(tag: Filters.ProductTag): String {
        return when (tag) {
            Filters.ProductTag.CATALOG -> R.string.productTag_catalog
            Filters.ProductTag.FACE -> R.string.productTag_face
            Filters.ProductTag.BODY -> R.string.productTag_body
            Filters.ProductTag.SUNTAN -> R.string.productTag_suntan
            Filters.ProductTag.MASK -> R.string.productTag_mask
        }.let(::getString)
    }

    private fun onFiltersWasChanged() {
        viewModel.getAllProducts(filters)
        dataObserverAnyChanges.needToMakeAction = true
    }

    private fun configureSpinnerSortingStrategy() {
        with(binding.fragCatalogSpinnerSortingStrategy) {
            val strategyTitles = getSortingStrategyTitles()
            adapter = createArrayAdapterForSpinnerSortingStrategy(strategyTitles)
            onItemSelectedListener = createAdapterViewForSpinnerSortingStrategy()
        }
    }

    private fun createAdapterViewForSpinnerSortingStrategy(): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val currentPosition = binding.fragCatalogSpinnerSortingStrategy.selectedItemPosition

                when (Filters.SortingStrategy.entries[currentPosition]) {
                    Filters.SortingStrategy.POPULARITY -> {
                        if (filters.sortingStrategy != Filters.SortingStrategy.POPULARITY) {
                            filters = filters.copy(
                                sortingStrategy = Filters.SortingStrategy.POPULARITY
                            )
                        }
                    }

                    Filters.SortingStrategy.PRICE_UP -> {
                        if (filters.sortingStrategy != Filters.SortingStrategy.PRICE_UP) {
                            filters = filters.copy(
                                sortingStrategy = Filters.SortingStrategy.PRICE_UP
                            )
                        }
                    }

                    Filters.SortingStrategy.PRICE_DOWN -> {
                        if (filters.sortingStrategy != Filters.SortingStrategy.PRICE_DOWN) {
                            filters = filters.copy(
                                sortingStrategy = Filters.SortingStrategy.PRICE_DOWN
                            )
                        }
                    }
                }
            }
        }
    }

    private fun createArrayAdapterForSpinnerSortingStrategy(items: List<String>): ArrayAdapter<String> {
        return ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            items
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_item)
        }
    }

    private fun getSortingStrategyTitles(): List<String> {
        return Filters.SortingStrategy.entries
            .map { strategy ->
                when (strategy) {
                    Filters.SortingStrategy.POPULARITY -> R.string.sortStrategy_popularity
                    Filters.SortingStrategy.PRICE_UP -> R.string.sortStrategy_priceUp
                    Filters.SortingStrategy.PRICE_DOWN -> R.string.sortStrategy_priceDown
                }
            }
            .map(::getString)
    }

    private fun observeToViewModelResults() {
        viewModel.productsLive.observe(viewLifecycleOwner, recyclerAdapter::updateItems)
    }

    private fun configureRecyclerView() {
        with(binding.fragCatalogRecyclerViewProducts) {
            val spaceCount = 2
            adapter = recyclerAdapter
            layoutManager = GridLayoutManager(requireContext(), spaceCount)
            addItemDecoration(GridSpacingItemDecoration(spaceCount, 7.dp, true))
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }

    private fun navToProductPage(product: Product) {
        CatalogFragmentDirections
            .actionCatalogFragmentToProductPageFragment(product)
            .let(findNavController()::navigate)
    }

    private fun likeProduct(product: Product) {
        if (product.isFavorite) viewModel.removeProductFromFavorites(product)
        else viewModel.addProductToFavorites(product)

        val updatedList = recyclerAdapter
            .getItems()
            .map { oldProduct ->
                with(oldProduct) {
                    if (id == product.id) copy(isFavorite = !isFavorite) else this
                }
            }

        recyclerAdapter.updateItems(updatedList)
    }

    companion object {
        private const val FILTERS_EXTRA = "filters"
    }

}