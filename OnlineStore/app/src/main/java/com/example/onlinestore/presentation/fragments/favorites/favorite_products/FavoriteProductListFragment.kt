package com.example.onlinestore.presentation.fragments.favorites.favorite_products

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.domain.models.Product
import com.example.onlinestore.R
import com.example.onlinestore.app.OnlineStoreApp
import com.example.onlinestore.databinding.ListProductsBinding
import com.example.onlinestore.presentation.fragments.favorites.main.FavoritesFragmentDirections
import com.example.onlinestore.presentation.recycler.adapters.ProductRecyclerAdapter
import com.example.onlinestore.presentation.recycler.decorators.GridSpacingItemDecoration
import com.example.onlinestore.presentation.utils.ViewModelFactory
import com.example.onlinestore.presentation.utils.dp
import javax.inject.Inject

class FavoriteProductListFragment : Fragment(R.layout.list_products) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory<FavoriteProductListViewModel>

    private val binding: ListProductsBinding by viewBinding()
    private val viewModel: FavoriteProductListViewModel by viewModels { viewModelFactory }

    private val recyclerAdapter = ProductRecyclerAdapter(
        ::navToProductPage,
        ::likeProduct,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as OnlineStoreApp).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeToViewModelResults()
        configureRecyclerView()

        viewModel.getFavoriteProducts()
    }

    private fun observeToViewModelResults() {
        viewModel.favoriteProductsLive.observe(viewLifecycleOwner, recyclerAdapter::updateItems)
    }

    private fun navToProductPage(product: Product) {
        FavoritesFragmentDirections
            .actionFavoritesFragmentToProductPageFragment(product)
            .let(findNavController()::navigate)
    }

    private fun configureRecyclerView() {
        with(binding.listProductsRecyclerView) {
            val spaceCount = 2
            adapter = recyclerAdapter
            layoutManager = GridLayoutManager(requireContext(), spaceCount)
            addItemDecoration(GridSpacingItemDecoration(spaceCount, 7.dp, true))
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        }
    }

    private fun likeProduct(product: Product) {
        viewModel.removeProductFromFavorites(product)

        val updatedList = recyclerAdapter
            .getItems()
            .filter { it.id != product.id }

        recyclerAdapter.updateItems(updatedList)
    }
}