package com.jdamcd.runlog.shared.util

expect fun String.format(p1: Int, p2: Int): String
expect fun String.format(p1: Int, p2: Int, p3: Int): String
expect fun Float.formatDecimal(pattern: String): String
expect fun String.formatDate(pattern: String): String
