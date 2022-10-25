package com.jdamcd.runlog.shared

expect fun Double.formatKm(): String
expect fun Long.formatDuration(): String
expect fun String.formatDate(pattern: String): String
