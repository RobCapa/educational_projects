package com.example.onlinestore.presentation.utils

import com.example.onlinestore.R

/**
 * This file was created only because the images were hardcoded in the technical specifications
 * */

object HardcodeImageList {
    fun getProductImages(productId: String): List<Int> {
        return hardcodeImages
            .first { it.id == productId }
            .imageIds
    }

    private val hardcodeImages = listOf(
        HardcodeImage(
            id = "cbf0c984-7c6c-4ada-82da-e29dc698bb50",
            imageIds = listOf(
                R.drawable.product_6,
                R.drawable.product_5,
            ),
        ),
        HardcodeImage(
            id = "54a876a5-2205-48ba-9498-cfecff4baa6e",
            imageIds = listOf(
                R.drawable.product_1,
                R.drawable.product_2,
            ),
        ),
        HardcodeImage(
            id = "75c84407-52e1-4cce-a73a-ff2d3ac031b3",
            imageIds = listOf(
                R.drawable.product_5,
                R.drawable.product_6,
            ),
        ),
        HardcodeImage(
            id = "16f88865-ae74-4b7c-9d85-b68334bb97db",
            imageIds = listOf(
                R.drawable.product_3,
                R.drawable.product_4,
            ),
        ),
        HardcodeImage(
            id = "26f88856-ae74-4b7c-9d85-b68334bb97db",
            imageIds = listOf(
                R.drawable.product_2,
                R.drawable.product_3,
            ),
        ),
        HardcodeImage(
            id = "15f88865-ae74-4b7c-9d81-b78334bb97db",
            imageIds = listOf(
                R.drawable.product_6,
                R.drawable.product_1,
            ),
        ),
        HardcodeImage(
            id = "88f88865-ae74-4b7c-9d81-b78334bb97db",
            imageIds = listOf(
                R.drawable.product_4,
                R.drawable.product_3,
            ),
        ),
        HardcodeImage(
            id = "55f58865-ae74-4b7c-9d81-b78334bb97db",
            imageIds = listOf(
                R.drawable.product_1,
                R.drawable.product_5,
            ),
        ),
    )
}

data class HardcodeImage(
    val id: String,
    val imageIds: List<Int>,
)

