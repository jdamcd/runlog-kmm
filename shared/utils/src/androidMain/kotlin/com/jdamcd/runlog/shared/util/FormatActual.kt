package com.jdamcd.runlog.shared.util

import java.text.DecimalFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

actual fun String.format(p1: Int, p2: Int): String =
    String.format(this, p1, p2)

actual fun String.format(p1: Int, p2: Int, p3: Int): String =
    String.format(this, p1, p2, p3)

actual fun Float.formatDecimal(pattern: String): String =
    DecimalFormat(pattern).format(this)

actual fun String.formatDate(pattern: String): String =
    ZonedDateTime
        .parse(this)
        .format(DateTimeFormatter.ofPattern(pattern))
