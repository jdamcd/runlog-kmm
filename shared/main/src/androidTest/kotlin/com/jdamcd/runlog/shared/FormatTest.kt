package com.jdamcd.runlog.shared

import io.kotest.matchers.shouldBe
import org.junit.Test

class FormatTest {

    @Test
    fun formatsMetresToKmWithSingleDecimal() {
        10_123.0.formatKm() shouldBe "10.1k"
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
}
