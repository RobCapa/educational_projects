package com.example.viewsanimations.interfaces

import com.example.viewsanimations.data.TimeState

interface Timer {
    fun start()
    fun stop()
    fun reset()
    fun currentTime():Long
    fun addUpdateListener(listener:(TimeState)->Unit)
    fun removeUpdateListener(listener:(TimeState)->Unit)
}