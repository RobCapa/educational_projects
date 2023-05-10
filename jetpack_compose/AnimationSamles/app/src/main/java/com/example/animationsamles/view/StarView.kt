package com.example.animationsamles.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.widget.TextView

class StarView : View {
    lateinit var myShape: StarShape
    var ratioRadius = 0.5f
    var ratioInnerRadius = 0.2F
    private var numberOfPoint = 3 //default

    //corresponding to UI element
    var textLayerInfo: TextView? = null

    constructor(context: Context?) : super(context) {
        initMyView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initMyView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initMyView()
    }

    private fun initMyView() {
        myShape = StarShape()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val starting = System.nanoTime()
        val w = width
        val h = height
        if (w == 0 || h == 0) {
            return
        }
        val x = w.toFloat() / 2.0f
        val y = h.toFloat() / 2.0f
        val radius: Float
        val innerRadius: Float
        if (w > h) {
            radius = h * ratioRadius
            innerRadius = h * ratioInnerRadius
        } else {
            radius = w * ratioRadius
            innerRadius = w * ratioInnerRadius
        }
        myShape.setStar(x, y, radius, innerRadius, numberOfPoint)
        canvas.drawPath(myShape.path, myShape.paint)
        val end = System.nanoTime()
        val info = """myView.isHardwareAccelerated() = $isHardwareAccelerated
canvas.isHardwareAccelerated() = ${canvas.isHardwareAccelerated}
processing time (reference only) : ${end - starting} (ns)"""
        textLayerInfo?.text = info
    }

    fun setShapeRadiusRatio(ratio: Float) {
        ratioRadius = ratio
    }

    fun setShapeInnerRadiusRatio(ratio: Float) {
        ratioInnerRadius = ratio
    }

    fun setNumberOfPoint(pt: Int) {
        if(pt != numberOfPoint) {
            numberOfPoint = pt
            invalidate()
        }
    }

    fun passElements(textLayerInfo: TextView?) {
        this.textLayerInfo = textLayerInfo
    }
}