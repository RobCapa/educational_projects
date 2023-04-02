package com.example.viewsanimations.utils

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes

fun Context.getResIdFromTheme(@AttrRes attrId: Int): Int {
    val value = TypedValue()
    this.theme.resolveAttribute(attrId, value, true)
    return value.resourceId
}