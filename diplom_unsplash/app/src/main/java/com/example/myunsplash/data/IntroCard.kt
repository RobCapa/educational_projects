package com.example.myunsplash.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class IntroCard(
    @DrawableRes val image: Int,
    @StringRes val text: Int,
)
