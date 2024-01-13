package ru.aston.utils

import android.content.Context
import android.widget.Button
import androidx.annotation.ColorInt
import androidx.collection.LongSparseArray
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import kotlin.random.Random
import kotlin.reflect.full.superclasses


fun Button.setBackgroundTint(@ColorInt color: Int) {
    background = DrawableCompat.wrap(background).apply {
        setTint(color)
    }
}

fun FragmentStateAdapter.getItem(position: Int): Fragment? {
    return this::class.superclasses.find { it == FragmentStateAdapter::class }
        ?.java?.getDeclaredField("mFragments")
        ?.let { field ->
            field.isAccessible = true
            val mFragments = field.get(this) as LongSparseArray<Fragment>
            return@let mFragments[getItemId(position)]
        }
}

fun IntProgression.random(): Int = toList().random()

fun Context.changeColorRandomly(colorsIds: List<Int>, shadeRange: IntProgression): Int {
    return getColor(colorsIds.random()).run {
        if (Random.nextBoolean()) plus(shadeRange.random())
        else minus(shadeRange.random())
    }
}