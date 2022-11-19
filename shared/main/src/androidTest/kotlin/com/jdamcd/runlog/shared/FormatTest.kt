package com.jdamcd.runlog.shared

import io.kotest.matchers.shouldBe
import org.junit.Test

class FormatTest {

    @Test
    fun formatsMetresToKmWithSingleDecimal() {
        10_123.0.formatKm() shouldBe "10.1k"
    }

    @Test
    fun formatsRoundKmsWithNoDecimal() {
        10_000.0.formatKm() shouldBe "10k"
    }

    @Test
    fun formatsDistanceOver1000KmWithoutDecimal() {
        1_001_234.5.formatKm() shouldBe "1,001k"
    }

    @Test
    fun formatsPacePerKmFromSeconds() {
        301.formatPace() shouldBe "5:01/k"
    }

    @Test
    fun formatDurationLessThan10Mins() {
        540L.formatDuration() shouldBe "09:00"
    }

    @Test
    fun formatsDurationLessThanAnHour() {
        3540L.formatDuration() shouldBe "59:00"
    }

    @Test
    fun formatsDurationMoreThanAnHour() {
        3660L.formatDuration() shouldBe "1:01:00"
    }

    @Test
    fun formatsDateWithPattern() {
        "2022-10-25T17:58:50Z".formatDate("dd MMM, k:mm") shouldBe "25 Oct, 17:58"
    }
}
