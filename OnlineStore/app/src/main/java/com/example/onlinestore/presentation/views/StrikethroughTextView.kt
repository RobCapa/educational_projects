package com.example.onlinestore.presentation.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class StrikethroughTextView(context: Context, attrs: AttributeSet) :
    AppCompatTextView(context, attrs) {

    private val paint = Paint()

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 3f
        paint.color = currentTextColor
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawLine(
            0f,
            height.toFloat() * 0.7f,
            width.toFloat(),
            height.toFloat() * 0.4f,
            paint
        )

        /*canvas.drawLine(
            0f,
            height.toFloat(),
            width.toFloat(),
            0f,
            paint
        )*/
    }
}