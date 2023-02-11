package com.jdamcd.runlog.shared.util

interface Log {
    fun debug(message: String)
    fun error(message: String)
}

class MultiLog : Log {

    private val output = LogOutput()

    override fun debug(message: String) {
        output.debug(TAG, message)
    }

    override fun error(message: String) {
        output.error(TAG, message)
    }
}

private const val TAG = "RunLogShared"

expect class LogOutput() {
    fun debug(tag: String, message: String)
    fun error(tag: String, message: String)
}
