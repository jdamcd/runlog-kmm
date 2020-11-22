package com.jdamcd.runlog.shared.util

object Logger {

    private const val TAG = "RunLog"
    private val output = LogOutput()

    fun debug(message: String) {
        output.debug(TAG, message)
    }

    fun error(message: String) {
        output.error(TAG, message)
    }
}

expect class LogOutput() {
    fun debug(tag: String, message: String)
    fun error(tag: String, message: String)
}
