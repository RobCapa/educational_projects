package com.example.viewsanimations.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.viewsanimations.R
import com.example.viewsanimations.data.TimeState
import com.example.viewsanimations.databinding.ViewTimerBinding
import com.example.viewsanimations.interfaces.Timer
import kotlinx.coroutines.*

class TimerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr), Timer {
    private val binding: ViewTimerBinding
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var timerJob: Job? = null
    private val listeners: MutableList<(TimeState) -> Unit> = mutableListOf()

    private var hour = 0
    private var minute = 0
    private var second = 0

    init {
        val root = inflate(context, R.layout.view_timer, this)
        binding = ViewTimerBinding.bind(root)
        binding.buttonStartOrStop.setOnClickListener { startOrStopTimer() }
        binding.buttonReset.setOnClickListener { reset() }
        printCurrentTime()
    }

    override fun start() {
        timerJob = coroutineScope.launch {
            while (true) {
                delay(1000)

                second += 1

                if (second == 60) {
                    minute += 1
                    second = 0
                }

                if (minute == 60) {
                    hour += 1
                    minute = 0
                }

                printCurrentTime()
                showCurrentTimeOnClockFace()
                notifyListeners()
            }
        }
    }

    override fun stop() {
        timerJob?.cancel()
        timerJob = null
        binding.buttonStartOrStop.text = context.getString(R.string.start)
        binding.buttonStartOrStop.setBackgroundColor(context.getColor(R.color.green))
        notifyListeners()
    }

    override fun reset() {
        stop()
        resetTime()
        showCurrentTimeOnClockFace()
        printCurrentTime()
        notifyListeners()
    }

    override fun currentTime(): Long {
        return (hour * 3600 + minute * 60 + second).toLong()
    }

    override fun addUpdateListener(listener: (TimeState) -> Unit) {
        listeners.add(listener)
    }

    override fun removeUpdateListener(listener: (TimeState) -> Unit) {
        listeners.remove(listener)
    }

    private fun startOrStopTimer() {
        if (timerJob == null) {
            start()
            binding.buttonStartOrStop.text = context.getString(R.string.stop)
            binding.buttonStartOrStop.setBackgroundColor(context.getColor(R.color.red))
        } else {
            stop()
        }
    }

    private fun notifyListeners() {
        listeners.forEach { listener ->
            listener(
                TimeState(
                    currentTime(),
                    timerJob != null
                )
            )
        }
    }

    private fun resetTime() {
        hour = 0
        minute = 0
        second = 0
    }

    private fun showCurrentTimeOnClockFace() {
        binding.clockView.setTime(
            hour,
            minute,
            second,
        )
    }

    @SuppressLint("SetTextI18n")
    private fun printCurrentTime() {
        binding.textViewTime.text = "" +
                "$hour : " +
                "${if (minute < 10) "0" else ""}$minute : " +
                "${if (second < 10) "0" else ""}$second"
    }
}