package ru.aston.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class NewsCategory(val title: String) : Parcelable {
    GENERAL("General"),
    BUSINESS("Business"),
    ENTERTAINMENT("Entertainment"),
    HEALTH("Health"),
    SCIENCE("Science"),
    TECHNOLOGY("Technology"),
    SPORT("Sport"),
}