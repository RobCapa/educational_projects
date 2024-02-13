package com.example.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Filters(
    val sortingStrategy: SortingStrategy = SortingStrategy.POPULARITY,
    val tags: List<ProductTag> = emptyList(),
): Parcelable {

    enum class SortingStrategy {
        POPULARITY,
        PRICE_UP,
        PRICE_DOWN,
    }

    enum class ProductTag(
        val title: String,
    ) {
        CATALOG("catalog"),
        FACE("face"),
        BODY("body"),
        SUNTAN("suntan"),
        MASK("mask"),
    }
}
