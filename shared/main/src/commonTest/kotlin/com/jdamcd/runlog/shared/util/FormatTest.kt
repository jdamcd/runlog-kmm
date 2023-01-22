package com.jdamcd.runlog.shared.util

import com.jdamcd.runlog.shared.util.Formatter.formatDuration
import com.jdamcd.runlog.shared.util.Formatter.formatElevation
import com.jdamcd.runlog.shared.util.Formatter.formatKm
import com.jdamcd.runlog.shared.util.Formatter.formatPace
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class FormatTest {

    @Test
    fun `formats metres to km with a single decimal`() {
        formatKm(10_123.0f) shouldBe "10.1k"
    }

    @Test
    fun `formats round kms without decimal`() {
        formatKm(10_000.0f) shouldBe "10k"
    }

    @Test
    fun `formats distances over 1000km without decimal`() {
        formatKm(1_001_234.5f) shouldBe "1,001k"
    }

    @Test
    fun `formats distance without unit`() {
        formatKm(10_123.0f, withUnit = false) shouldBe "10.1"
    }

    @Test
    fun `formats elevation with a single decimal`() {
        formatElevation(123.45f) shouldBe "123.4m"
    }

    @Test
    fun `formats round elevation with no decimal`() {
        formatElevation(123.0f) shouldBe "123m"
    }

    @Test
    fun `formats elevation without unit`() {
        formatElevation(123.45f, withUnit = false) shouldBe "123.4"
    }

    @Test
    fun `formats pace per km from seconds`() {
        formatPace(301) shouldBe "5:01 /km"
    }

    @Test
    fun `formats pace without unit`() {
        formatPace(301, withUnit = false) shouldBe "5:01"
    }

    @Test
    fun `format duration less than 10 mins`() {
        formatDuration(540) shouldBe "9:00"
    }

    @Test
    fun `formats duration less than one hour`() {
        formatDuration(3540) shouldBe "59:00"
    }

    @Test
    fun `formats duration more than one hour`() {
        formatDuration(3660) shouldBe "1:01:00"
    }

    @Test
    fun `formats ISO8601 date with Java style formatting pattern`() {
        "2022-10-25T17:58:50Z".formatDate("dd MMM, k:mm") shouldBe "25 Oct, 17:58"
    }
}
