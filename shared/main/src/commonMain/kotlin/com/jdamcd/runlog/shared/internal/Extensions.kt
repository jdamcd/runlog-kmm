package com.jdamcd.runlog.shared.internal

import kotlin.math.roundToInt

expect fun Float.formatKm(withUnit: Boolean = true): String
expect fun Float.formatElevation(withUnit: Boolean = true): String
expect fun Int.formatPace(withUnit: Boolean = true): String
expect fun Int.formatDuration(): String
expect fun String.formatDate(pattern: String): String

internal object Utils {

    fun calculatePace(distanceMetres: Float, timeSeconds: Int): Int {
        val distanceKm = distanceMetres / 1000
        return (timeSeconds / distanceKm).roundToInt() // seconds per km
    }
}