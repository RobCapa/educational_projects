package com.example.onlinestore.presentation.fragments.profile

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.onlinestore.R

enum class ProfileAction(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    @ColorRes val iconColor: Int,
) {
    FAVORITES(
        R.string.fragProfile_action_favorites,
        R.drawable.ic_heart_hollow,
        R.color.pink_400,
    ),
    SHOPS(
        R.string.fragProfile_action_shops,
        R.drawable.ic_shop,
        R.color.pink_400,
    ),
    FEEDBACK(
        R.string.fragProfile_action_feedback,
        R.drawable.ic_dialog,
        R.color.orange_500,
    ),
    OFFER(
        R.string.fragProfile_action_offer,
        R.drawable.ic_document,
        R.color.gray_500,
    ),
    PRODUCT_RETURN(
        R.string.fragProfile_action_productReturn,
        R.drawable.ic_return,
        R.color.gray_500,
    ),
}