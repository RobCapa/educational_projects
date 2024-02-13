package com.example.onlinestore.presentation.fragments.favorites.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.onlinestore.R
import com.example.onlinestore.databinding.FragFavouritesBinding
import com.example.onlinestore.presentation.fragments.favorites.favorite_brands.FavoriteBrandListFragment
import com.example.onlinestore.presentation.fragments.favorites.favorite_products.FavoriteProductListFragment
import com.example.onlinestore.presentation.fragments.favorites.main.utils.ViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class FavoritesFragment : Fragment(R.layout.frag_favourites) {

    private val binding: FragFavouritesBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureTabs()
        setOnClickListeners()
        binding.fragFavoritesViewPager.isUserInputEnabled = false
    }

    private fun configureTabs() {
        val tabs = FavoriteTab.entries

        val viewPagerAdapter = ViewPagerAdapter(childFragmentManager, lifecycle).apply {
            tabs.forEach { tab ->
                when (tab) {
                    FavoriteTab.PRODUCTS -> {
                        addFragment(FavoriteProductListFragment())
                    }

                    FavoriteTab.BRANDS -> {
                        addFragment(FavoriteBrandListFragment())
                    }
                }
            }

        }

        with(binding) {
            fragFavoritesViewPager.adapter = viewPagerAdapter

            TabLayoutMediator(
                fragFavoritesTabLayout,
                fragFavoritesViewPager,
            ) { tab, position ->
                tab.text = getString(tabs[position].tabName)
            }.attach()
        }
    }

    private fun setOnClickListeners() {
        binding.fragFavoritesTopAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}