package com.example.onlinestore.presentation.utils

import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

class ViewPagerOnTouchListener(
    var onClickOnItem: () -> Unit,
) : View.OnTouchListener {

    private var startX = 0f
    private var startY = 0f

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }

            MotionEvent.ACTION_UP -> {
                val endX = event.x
                val endY = event.y
                val deltaX = abs(endX - startX)
                val deltaY = abs(endY - startY)

                if (deltaX < 10 && deltaY < 10) onClickOnItem()
            }
        }
        return false
    }
}