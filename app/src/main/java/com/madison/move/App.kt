package com.madison.move

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        init(this)
    }

    companion object {
        var instance: App? = null
            private set

        private fun init(app: App) {
            instance = app
        }
    }
}