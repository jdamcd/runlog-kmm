package com.jdamcd.runlog.shared

import platform.Foundation.NSString
import platform.Foundation.stringWithFormat

actual fun Double.formatKm(): String {
    return NSString.stringWithFormat("%.1fk", this / 1000)
}

actual fun Long.formatDuration(): String {
    val hours = this / 3600
    val mins = (this % 3600) / 60
    val seconds = this % 60
    return if (hours > 0) {
        NSString.stringWithFormat("%d:%02d:%02d", hours, mins, seconds)
    } else {
        NSString.stringWithFormat("%02d:%02d", mins, seconds)
    }
}
