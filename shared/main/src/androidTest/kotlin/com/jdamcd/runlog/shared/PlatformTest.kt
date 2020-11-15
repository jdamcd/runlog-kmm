package com.jdamcd.runlog.shared

import io.kotest.matchers.string.shouldContain
import org.junit.Test

class PlatformTest {

    @Test
    fun hasCorrectPlatformName() {
        Platform().platform shouldContain "Android"
    }
}
