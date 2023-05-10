package com.example.animationsamles.view

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import kotlin.math.cos
import kotlin.math.sin

class StarShape {
    val paint: Paint = Paint()
    val path: Path = Path()

    init {
        paint.color = Color.BLUE
        paint.strokeWidth = 3f
        paint.style = Paint.Style.STROKE
    }

    fun setCircle(x: Float, y: Float, radius: Float, dir: Path.Direction) {
        path.reset()
        path.addCircle(x, y, radius, dir)
    }

    fun setStar(x: Float, y: Float, radius: Float, innerRadius: Float, numOfPt: Int) {
        val section = 2.0 * Math.PI / numOfPt
        path.reset()
        path.moveTo(
            (x + radius * cos(0.0)).toFloat(),
            (y + radius * sin(0.0)).toFloat()
        )
        path.lineTo(
            (x + innerRadius * cos(0 + section / 2.0)).toFloat(),
            (y + innerRadius * sin(0 + section / 2.0)).toFloat()
        )
        for (i in 1 until numOfPt) {
            path.lineTo(
                (x + radius * cos(section * i)).toFloat(),
                (y + radius * sin(section * i)).toFloat()
            )
            path.lineTo(
                (x + innerRadius * cos(section * i + section / 2.0)).toFloat(),
                (y + innerRadius * sin(section * i + section / 2.0)).toFloat()
            )
        }
        path.close()
    }
}