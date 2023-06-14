package com.madison.move.ui.offlinechannel

import android.os.Handler
import android.os.Looper
import android.util.Log

interface DataCallback {
    fun onDataReceived(value: Int)
}


object TimeCounter {
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var isRunning: Boolean = false
    private var timeInSeconds: Int = 0
    private var callback: DataCallback? = null

    fun initialize() {
        handler = Handler(Looper.getMainLooper())
        runnable = Runnable {
            timeInSeconds++
            handler.postDelayed(runnable, 1000)
            if (timeInSeconds >= 30) {
                callback?.onDataReceived(timeInSeconds)
                resetTimer()
                handler.removeCallbacks(runnable)
            }
        }
    }

    fun startTimer() {
        if (!isRunning) {
            handler.postDelayed(runnable, 1000)
            isRunning = true
        }
    }

    fun pauseTimer() {
        if (isRunning) {
            handler.removeCallbacks(runnable)
            isRunning = false
        }
    }

    fun resetTimer() {
        timeInSeconds = 0
    }

    fun getTimeInSeconds(): Int {
        return timeInSeconds
    }

    fun setTimeInSeconds(oldTime: Int) {
        timeInSeconds = oldTime
    }

    fun setCallback(callback: DataCallback) {
        this.callback = callback
    }

}