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

actual fun Float.formatKm(withUnit: Boolean): String {
    val distanceKm = this / 1000
    val nf = NSNumberFormatter()
    nf.positiveFormat = if (distanceKm >= 1000) "#,##0" else "#,##0.#"
    val unit = if (withUnit) "k" else ""
    return "${nf.stringFromNumber(NSNumber(distanceKm))}$unit"
}

actual fun Float.formatElevation(withUnit: Boolean): String {
    val nf = NSNumberFormatter()
    nf.positiveFormat = "#0.#"
    val unit = if (withUnit) "m" else ""
    return "${nf.stringFromNumber(NSNumber(this))}$unit"
}

actual fun Int.formatPace(withUnit: Boolean): String {
    val mins = this / 60
    val seconds = this % 60
    val pattern = if (withUnit) "%d:%02d /km" else "%d:%02d"
    return NSString.stringWithFormat(pattern, mins, seconds)
}

actual fun Int.formatDuration(): String {
    val hours = this / 3600
    val mins = (this % 3600) / 60
    val seconds = this % 60
    return if (hours > 0) {
        NSString.stringWithFormat("%d:%02d:%02d", hours, mins, seconds)
    } else {
        NSString.stringWithFormat("%d:%02d", mins, seconds)
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
