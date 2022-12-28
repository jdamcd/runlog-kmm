package com.jdamcd.runlog.shared.internal

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

actual fun String.format(p1: Int, p2: Int): String =
    NSString.stringWithFormat(this, p1, p2)

actual fun String.format(p1: Int, p2: Int, p3: Int): String =
    NSString.stringWithFormat(this, p1, p2, p3)

actual fun Float.formatDecimal(pattern: String): String {
    val nf = NSNumberFormatter()
    nf.positiveFormat = pattern
    return nf.stringFromNumber(NSNumber(this)) ?: ""
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
