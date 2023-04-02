package com.example.viewsanimations.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.example.viewsanimations.R
import com.example.viewsanimations.utils.getResIdFromTheme
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


class ClockFaceView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {
    private var mHeight = 0
    private var mWidth = 0
    private val mClockHours = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)

    private var mPadding = 0
    private val mNumeralSpacing = 0

    private var mHandTruncation = 0

    private var mHourHandTruncation = 0

    private var mRadius = 0
    private var mPaint: Paint? = null
    private val mRect: Rect = Rect()
    private var isInit = false

    private var hour = 12
    private var minute = 0
    private var second = 0

    fun setTime(
        hour: Int = 12,
        minute: Int = 0,
        second: Int = 0,
    ) {
        this.hour = hour
        this.minute = minute
        this.second = second
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        if (!isInit) {
            mPaint = Paint()
            mHeight = height
            mWidth = width
            mPadding = mNumeralSpacing + 50
            val minAttr = min(mHeight, mWidth)
            mRadius = minAttr / 2 - mPadding

            mHandTruncation = minAttr / 20
            mHourHandTruncation = minAttr / 17
            isInit = true
        }

        canvas!!

        mPaint?.let {
            with(it) {
                reset()
                color = context.getColor(context.getResIdFromTheme(R.attr.clockFaceColor))
                style = Paint.Style.STROKE
                strokeWidth = 4f
                isAntiAlias = true
                canvas.drawCircle(mWidth / 2f, mHeight / 2f, mRadius + mPadding - 10f, this)

                style = Paint.Style.FILL
                canvas.drawCircle(mWidth / 2f, mHeight / 2f, 12f, this)
            }
        }

        val fontSize = TypedValue
            .applyDimension(TypedValue.COMPLEX_UNIT_SP, 14f, resources.displayMetrics)
            .toInt()
        mPaint!!.textSize = fontSize.toFloat()

        for (hour in mClockHours) {
            val tmp = hour.toString()
            mPaint!!.getTextBounds(tmp, 0, tmp.length, mRect)

            val angle = Math.PI / 6 * (hour - 3)
            val x = (mWidth / 2 + cos(angle) * mRadius - mRect.width() / 2).toInt()
            val y = (mHeight / 2 + sin(angle) * mRadius + mRect.height() / 2).toInt()
            canvas.drawText(
                hour.toString(),
                x.toFloat(),
                y.toFloat(),
                mPaint!!
            )
        }

        drawHandLine(
            canvas,
            (hour + minute / 60f) * 5,
            isHour = true,
            isSecond = false
        )

        drawHandLine(
            canvas,
            (minute + second / 60f) * 5,
            isHour = false,
            isSecond = false
        )

        drawHandLine(
            canvas,
            second.toFloat(),
            isHour = false,
            isSecond = true
        )
    }

    private fun drawHandLine(canvas: Canvas, moment: Float, isHour: Boolean, isSecond: Boolean) {
        val angle = Math.PI * moment / 30 - Math.PI / 2
        val handRadius =
            if (isHour) mRadius - mHandTruncation - mHourHandTruncation
            else mRadius - mHandTruncation
        if (isSecond) mPaint!!.color =
            context.getColor(context.getResIdFromTheme(R.attr.accentColor))
        canvas.drawLine(
            (mWidth / 2).toFloat(),
            (mHeight / 2).toFloat(),
            (mWidth / 2 + cos(angle) * handRadius).toFloat(),
            (mHeight / 2 + sin(angle) * handRadius).toFloat(),
            mPaint!!
        )
    }
}