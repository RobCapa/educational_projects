package com.example.domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    val id: String,
    val title: String,
    val subtitle: String,
    val available: Int,
    val description: String,
    val ingredients: String,
    val isFavorite: Boolean = false,
    val tags: List<String>,
    val info: List<Info>,
    val price: Price,
    val feedback: Feedback,
) : Parcelable

@Parcelize
data class Price(
    val price: Int,
    val discount: Int,
    val priceWithDiscount: Int,
    val unit: String,
) : Parcelable

@Parcelize
data class Feedback(
    val count: Int,
    val rating: Float,
) : Parcelable

@Parcelize
data class Info(
    val title: String,
    val value: String,
) : Parcelable