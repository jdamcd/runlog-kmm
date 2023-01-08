package com.jdamcd.runlog.shared.util

import com.jdamcd.runlog.shared.Result
import com.jdamcd.runlog.shared.api.AuthException
import kotlin.math.roundToInt

expect fun String.format(p1: Int, p2: Int): String
expect fun String.format(p1: Int, p2: Int, p3: Int): String
expect fun Float.formatDecimal(pattern: String): String
expect fun String.formatDate(pattern: String): String

internal object Formatter {

    fun formatKm(distance: Float, withUnit: Boolean = true): String {
        val distanceKm = distance / 1000
        val pattern = if (distanceKm >= 1000) "#,##0" else "#,##0.#"
        val unit = if (withUnit) "k" else ""
        return "${distanceKm.formatDecimal(pattern)}$unit"
    }

    fun formatElevation(meters: Float, withUnit: Boolean = true): String {
        val unit = if (withUnit) "m" else ""
        return "${meters.formatDecimal("#0.#")}$unit"
    }

    fun formatPace(secs: Int, withUnit: Boolean = true): String {
        val mins = secs / 60
        val seconds = secs % 60
        val pattern = if (withUnit) "%d:%02d /km" else "%d:%02d"
        return pattern.format(mins, seconds)
    }

    fun formatDuration(secs: Int): String {
        val hours = secs / 3600
        val mins = (secs % 3600) / 60
        val seconds = secs % 60
        return if (hours > 0) {
            "%d:%02d:%02d".format(hours, mins, seconds)
        } else {
            "%d:%02d".format(mins, seconds)
        }
    }
}

internal object Utils {

    fun calculatePace(distanceMetres: Float, timeSeconds: Int): Int {
        val distanceKm = distanceMetres / 1000
        return (timeSeconds / distanceKm).roundToInt() // seconds per km
    }
}

inline fun <T> tryCall(call: () -> Result<T>): Result<T> {
    return try {
        call.invoke()
    } catch (error: Throwable) {
        Result.Error(error, recoverable = error !is AuthException)
    }
}
