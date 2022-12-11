package com.jdamcd.runlog.shared.internal

import kotlin.math.roundToInt

internal object Utils {

    fun calculatePace(distanceMetres: Float, timeSeconds: Int): Int {
        val distanceKm = distanceMetres / 1000
        return (timeSeconds / distanceKm).roundToInt() // seconds per km
    }
}
