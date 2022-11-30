package com.jdamcd.runlog.shared

expect fun Float.formatKm(withUnit: Boolean = true): String
expect fun Float.formatElevation(withUnit: Boolean = true): String
expect fun Int.formatPace(withUnit: Boolean = true): String
expect fun Int.formatDuration(): String
expect fun String.formatDate(pattern: String): String
