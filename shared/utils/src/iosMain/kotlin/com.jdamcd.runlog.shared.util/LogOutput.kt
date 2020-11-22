package com.jdamcd.runlog.shared.util

actual class LogOutput actual constructor() {

    actual fun debug(tag: String, message: String) {
        println("$tag/Debug: $message")
    }

    actual fun error(tag: String, message: String) {
        println("$tag/Error: $message")
    }
}
