package com.example.onlinestore.presentation.utils

import android.content.res.Resources.getSystem

val Int.dp: Int get() = (this * getSystem().displayMetrics.density).toInt()
