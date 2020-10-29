package com.jdamcd.runlog.shared

import io.kotest.matchers.string.shouldContain
import kotlin.test.Test

class PlatformTest {

    @Test
    fun hasCorrectPlatformName() {
        Platform().platform shouldContain "iOS"
    }
}
