@file:OptIn(kotlin.time.ExperimentalTime::class)

package com.jdamcd.runlog.shared.testutil

import com.jdamcd.runlog.shared.util.Log
import kotlin.time.Clock
import kotlin.time.Instant

object MockClock : Clock {

    var epochSeconds: Long = 123L

    override fun now(): Instant = Instant.fromEpochSeconds(epochSeconds)
}

class MockLog : Log {

    override fun debug(message: String) {
        println(message)
    }

    override fun error(message: String) {
        println(message)
    }
}
