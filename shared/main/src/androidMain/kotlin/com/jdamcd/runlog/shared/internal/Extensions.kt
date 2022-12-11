package com.jdamcd.runlog.shared.internal

import java.text.DecimalFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

actual fun Float.formatKm(withUnit: Boolean): String {
    val distanceKm = this / 1000
    val pattern = if (distanceKm >= 1000) "#,##0" else "#,##0.#"
    val unit = if (withUnit) "k" else ""
    return "${DecimalFormat(pattern).format(distanceKm)}$unit"
}

actual fun Float.formatElevation(withUnit: Boolean): String {
    val unit = if (withUnit) "m" else ""
    return "${DecimalFormat("#0.#").format(this)}$unit"
}

actual fun Int.formatPace(withUnit: Boolean): String {
    val mins = this / 60
    val seconds = this % 60
    val pattern = if (withUnit) "%d:%02d /km" else "%d:%02d"
    return String.format(pattern, mins, seconds)
}

actual fun Int.formatDuration(): String {
    val hours = this / 3600
    val mins = (this % 3600) / 60
    val seconds = this % 60
    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, mins, seconds)
    } else {
        String.format("%d:%02d", mins, seconds)
    }
}

actual fun String.formatDate(pattern: String): String {
    return ZonedDateTime
        .parse(this)
        .format(DateTimeFormatter.ofPattern(pattern))
}
