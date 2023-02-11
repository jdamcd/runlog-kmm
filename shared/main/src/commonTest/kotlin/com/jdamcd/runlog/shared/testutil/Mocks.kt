package com.jdamcd.runlog.shared.testutil

import com.jdamcd.runlog.shared.util.Log
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

object MockClock : Clock {

    var epochSeconds: Long = 123L

    override fun now(): Instant {
        return Instant.fromEpochSeconds(epochSeconds)
    }
}

class MockLog : Log {

    override fun debug(message: String) {
        println(message)
    }

    override fun error(message: String) {
        println(message)
    }
}
