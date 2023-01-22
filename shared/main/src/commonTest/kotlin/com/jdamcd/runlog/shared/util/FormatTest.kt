package com.jdamcd.runlog.shared.util

import com.jdamcd.runlog.shared.util.Formatter.formatDuration
import com.jdamcd.runlog.shared.util.Formatter.formatElevation
import com.jdamcd.runlog.shared.util.Formatter.formatKm
import com.jdamcd.runlog.shared.util.Formatter.formatPace
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class FormatTest {

    @Test
    fun formatsMetresToKmWithSingleDecimal() {
        formatKm(10_123.0f) shouldBe "10.1k"
    }

    @Test
    fun formatsRoundKmsWithNoDecimal() {
        formatKm(10_000.0f) shouldBe "10k"
    }

    @Test
    fun formatsDistanceOver1000KmWithoutDecimal() {
        formatKm(1_001_234.5f) shouldBe "1,001k"
    }

    @Test
    fun formatsDistanceWithoutUnit() {
        formatKm(10_123.0f, withUnit = false) shouldBe "10.1"
    }

    @Test
    fun formatsElevationWithSingleDecimal() {
        formatElevation(123.45f) shouldBe "123.4m"
    }

    @Test
    fun formatsRoundElevationWithNoDecimal() {
        formatElevation(123.0f) shouldBe "123m"
    }

    @Test
    fun formatsElevationWithoutUnit() {
        formatElevation(123.45f, withUnit = false) shouldBe "123.4"
    }

    @Test
    fun formatsPacePerKmFromSeconds() {
        formatPace(301) shouldBe "5:01 /km"
    }

    @Test
    fun formatsPaceWithoutUnit() {
        formatPace(301, withUnit = false) shouldBe "5:01"
    }

    @Test
    fun formatDurationLessThan10Mins() {
        formatDuration(540) shouldBe "9:00"
    }

    @Test
    fun formatsDurationLessThanAnHour() {
        formatDuration(3540) shouldBe "59:00"
    }

    @Test
    fun formatsDurationMoreThanAnHour() {
        formatDuration(3660) shouldBe "1:01:00"
    }

    @Test
    fun formatsDateWithPattern() {
        "2022-10-25T17:58:50Z".formatDate("dd MMM, k:mm") shouldBe "25 Oct, 17:58"
    }
}
