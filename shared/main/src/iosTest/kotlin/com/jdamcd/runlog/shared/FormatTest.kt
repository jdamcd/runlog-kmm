package com.jdamcd.runlog.shared

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class FormatTest {

    @Test
    fun formatsMetresToKmWithSingleDecimal() {
        10_123.0f.formatKm() shouldBe "10.1k"
    }

    @Test
    fun formatsRoundKmsWithNoDecimal() {
        10_000.0f.formatKm() shouldBe "10k"
    }

    @Test
    fun formatsElevationWithSingleDecimal() {
        123.45f.formatElevation() shouldBe "123.4m"
    }

    @Test
    fun formatsRoundElevationWithNoDecimal() {
        123.0f.formatElevation() shouldBe "123m"
    }

    @Test
    fun formatsDistanceOver1000KmWithoutDecimal() {
        1_001_234.5f.formatKm() shouldBe "1,001k"
    }

    @Test
    fun formatsPacePerKmFromSeconds() {
        301.formatPace() shouldBe "5:01 /km"
    }

    @Test
    fun formatsPaceWithoutUnit() {
        301.formatPace(withUnit = false) shouldBe "5:01"
    }

    @Test
    fun formatDurationLessThan10Mins() {
        540.formatDuration() shouldBe "9:00"
    }

    @Test
    fun formatsDurationLessThanAnHour() {
        3540.formatDuration() shouldBe "59:00"
    }

    @Test
    fun formatsDurationMoreThanAnHour() {
        3660.formatDuration() shouldBe "1:01:00"
    }

    @Test
    fun formatsDateWithPattern() {
        "2022-10-25T17:58:50Z".formatDate("dd MMM, k:mm") shouldBe "25 Oct, 17:58"
    }
}
