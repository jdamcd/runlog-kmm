package com.jdamcd.runlog.shared

actual fun Double.formatKm(): String {
    return "%.1fk".format(this / 1000)
}

actual fun Long.formatDuration(): String {
    val hours = this / 3600
    val mins = (this % 3600) / 60
    val seconds = this % 60
    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, mins, seconds)
    } else {
        String.format("%02d:%02d", mins, seconds)
    }
}
