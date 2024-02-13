package com.example.onlinestore.presentation.fragments.favorites.main

import androidx.annotation.StringRes
import com.example.onlinestore.R

enum class FavoriteTab(
    @StringRes val tabName: Int,
) {
    PRODUCTS(R.string.fragFavorites_tabTitle_products),
    BRANDS(R.string.fragFavorites_tabTitle_brands),
}