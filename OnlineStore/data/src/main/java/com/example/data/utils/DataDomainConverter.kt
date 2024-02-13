package com.example.data.utils

import com.example.data.models.DFeedback
import com.example.data.models.DInfo
import com.example.data.models.DPrice
import com.example.data.models.DProduct
import com.example.data.models.DUser
import com.example.domain.models.Feedback
import com.example.domain.models.Info
import com.example.domain.models.Price
import com.example.domain.models.Product
import com.example.domain.models.User

object DataDomainConverter {

    fun convertUserToDUser(user: User): DUser {
        return with(user) {
            DUser(
                id = id,
                firstName = firstName,
                lastName = lastName,
                phoneNumber = phoneNumber,
            )
        }
    }

    fun convertDProductToProduct(dProduct: DProduct): Product {
        return with(dProduct) {
            Product(
                id = id,
                title = title,
                subtitle = subtitle,
                available = available,
                description = description,
                ingredients = ingredients,
                tags = tags,
                info = info.map(::convertDInfoToInfo),
                price = convertDPriceToPrice(price),
                feedback = convertDFeedbackToFeedback(feedback),
            )
        }
    }

    private fun convertDInfoToInfo(dInfo: DInfo): Info {
        return with(dInfo) {
            Info(
                title = title,
                value = value,
            )
        }
    }

    private fun convertDPriceToPrice(dPrice: DPrice): Price {
        return with(dPrice) {
            Price(
                price = price,
                discount = discount,
                priceWithDiscount = priceWithDiscount,
                unit = unit,
            )
        }
    }

    private fun convertDFeedbackToFeedback(dFeedback: DFeedback): Feedback {
        return with(dFeedback) {
            Feedback(
                count = count,
                rating = rating,
            )
        }
    }
}