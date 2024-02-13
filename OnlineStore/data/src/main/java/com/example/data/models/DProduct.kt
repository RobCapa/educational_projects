package com.example.data.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DProductsWrapper(
    @Json(name = "items")
    val items: List<DProduct>,
)

@JsonClass(generateAdapter = true)
data class DProduct(
    @Json(name = "id")
    val id: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "subtitle")
    val subtitle: String,
    @Json(name = "available")
    val available: Int,
    @Json(name = "description")
    val description: String,
    @Json(name = "ingredients")
    val ingredients: String,
    @Json(name = "tags")
    val tags: List<String>,
    @Json(name = "info")
    val info: List<DInfo>,
    @Json(name = "price")
    val price: DPrice,
    @Json(name = "feedback")
    val feedback: DFeedback,
)

@JsonClass(generateAdapter = true)
data class DPrice(
    @Json(name = "price")
    val price: Int,
    @Json(name = "discount")
    val discount: Int,
    @Json(name = "priceWithDiscount")
    val priceWithDiscount: Int,
    @Json(name = "unit")
    val unit: String,
)

@JsonClass(generateAdapter = true)
data class DFeedback(
    @Json(name = "count")
    val count: Int,
    @Json(name = "rating")
    val rating: Float,
)

@JsonClass(generateAdapter = true)
data class DInfo(
    @Json(name = "title")
    val title: String,
    @Json(name = "value")
    val value: String,
)
