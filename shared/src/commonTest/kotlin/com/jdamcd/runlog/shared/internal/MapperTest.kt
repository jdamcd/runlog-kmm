package com.jdamcd.runlog.shared.internal

import com.jdamcd.runlog.shared.ActivityCard
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class MapperTest {

    @Test
    fun mapsApiActivityToActivityCard() {
        val apiModel = ApiSummaryActivity(
            id = 123L,
            name = "my activity",
            type = "Run",
            distance = 10100.0,
            moving_time = 2400L
        )

        Mapper.mapActivityRow(apiModel) shouldBe ActivityCard(
            id = 123L,
            name = "my activity",
            type = "Run",
            distance = "10.1k",
            movingTime = "40:00"
        )
    }
}