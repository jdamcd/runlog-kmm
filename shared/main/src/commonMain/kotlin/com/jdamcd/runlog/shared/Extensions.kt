package com.jdamcd.runlog.shared

expect fun Float.formatKm(): String
expect fun Float.formatElevation(): String
expect fun Int.formatPace(withUnit: Boolean = true): String
expect fun Int.formatDuration(): String
expect fun String.formatDate(pattern: String): String
