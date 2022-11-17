package com.jdamcd.runlog.shared

import java.text.DecimalFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

actual fun Double.formatKm(): String {
    return "${DecimalFormat("#,##0.#").format(this / 1000)}k"
}

actual fun Int.formatPace(): String {
    val mins = this / 60
    val seconds = this % 60
    return String.format("%d:%02d/k", mins, seconds)
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

actual fun String.formatDate(pattern: String): String {
    return ZonedDateTime
        .parse(this)
        .format(DateTimeFormatter.ofPattern(pattern))
}
