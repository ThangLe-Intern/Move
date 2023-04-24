package com.madison.move.utils

import java.util.concurrent.Executor
import java.util.concurrent.Executors

class DiskExecutor : Executor {
    private val diskExecutor: Executor

    init {
        diskExecutor = Executors.newSingleThreadExecutor()
    }

    override fun execute(runnable: Runnable) {
        diskExecutor.execute(runnable)
    }
}