package com.jdamcd.runlog.shared

import platform.Foundation.NSDateFormatter
import platform.Foundation.NSISO8601DateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSString
import platform.Foundation.NSTimeZone
import platform.Foundation.autoupdatingCurrentLocale
import platform.Foundation.stringWithFormat
import platform.Foundation.timeZoneWithName

actual fun Double.formatKm(): String {
    val nf = NSNumberFormatter()
    nf.positiveFormat = "#,##0.#"
    return "${ nf.stringFromNumber(NSNumber(this / 1000))}k"
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

actual fun String.formatDate(pattern: String): String {
    return NSISO8601DateFormatter().dateFromString(this)?.let {
        NSDateFormatter().apply {
            this.timeZone = NSTimeZone.timeZoneWithName("UTC")!!
            this.locale = NSLocale.autoupdatingCurrentLocale
            this.dateFormat = pattern
        }.stringFromDate(it)
    } ?: ""
}
