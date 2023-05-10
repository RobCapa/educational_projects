package com.example.animationsamles.combineview

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.animationsamles.R

class CounterView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val leftButton: Button
    private val rightButton: Button
    private val counterView: TextView
    private var counterListeners = mutableSetOf<(Int) -> Unit>()

    var counter = 0
        set(value) {
            if (value == field)
                return
            field = value
            counterView.text = value.toString()
            counterListeners.forEach { it(value) }
        }

    init {
        val root = inflate(context, R.layout.view_combine, this)
        leftButton = root.findViewById(R.id.leftButton)
        rightButton = root.findViewById(R.id.rightButton)
        counterView = root.findViewById(R.id.counterText)

        leftButton.setOnClickListener { counter-- }
        rightButton.setOnClickListener { counter++ }
    }

    fun addListener(listener: (Int) -> Unit) {
        counterListeners.add(listener)
        listener(counter)
    }

    fun removeListener(listener: (Int) -> Unit) = counterListeners.remove(listener)
}