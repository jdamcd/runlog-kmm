package com.jdamcd.runlog.shared.util

import platform.Foundation.NSLog

actual class LogOutput {

    actual fun debug(tag: String, message: String) {
        NSLog("$tag/Debug: $message")
    }

    actual fun error(tag: String, message: String) {
        NSLog("$tag/Error: $message")
    }
}
