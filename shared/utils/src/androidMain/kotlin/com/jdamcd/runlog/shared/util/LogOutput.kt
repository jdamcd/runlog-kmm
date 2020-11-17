package com.jdamcd.runlog.shared.util

import android.util.Log

actual class LogOutput actual constructor() {

    actual fun debug(tag: String, message: String) {
        Log.d(tag, message)
    }

    actual fun error(tag: String, message: String) {
        Log.e(tag, message)
    }
}
