package com.example.recyclerview.paging

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

internal class MainThreadExecutor : Executor {
    private val mHandler: Handler = Handler(Looper.getMainLooper())
    override fun execute(p0: Runnable?) {
        p0?.let { mHandler.post(it) }
    }
}
