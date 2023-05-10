package com.example.compose.screens.bars.bottom_nav

import com.example.compose.R

sealed class BottomItem(
    val title: String,
    val iconId: Int,
    val route: String,
) {
    object PageHome : BottomItem("Home", R.drawable.ic_baseline_home, page_home)
    object PageProfile : BottomItem("Personages", R.drawable.ic_baseline_profile, page_profile)

    companion object {
        const val page_profile = "page_profile"
        const val page_home = "page_home"
    }
}