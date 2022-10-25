package com.jdamcd.runlog.shared.internal

import com.jdamcd.runlog.shared.ActivityCard
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class MapperTest {

    @Test
    fun mapsDefaultActivity() {
        Mapper.mapActivityRow(apiModel()) shouldBe ActivityCard(
            id = 123L,
            name = "my activity",
            type = "Run",
            label = null,
            distance = "10.1k",
            duration = "40:00",
            start = "Tuesday 25 Oct @ 5:58pm"
        )
    }

    @Test
    fun mapsRaceActivityUsingElapsedTime() {
        Mapper.mapActivityRow(apiModel(workout_type = 1)) shouldBe ActivityCard(
            id = 123L,
            name = "my activity",
            type = "Run",
            label = "Race",
            distance = "10.1k",
            duration = "41:00",
            start = "Tuesday 25 Oct @ 5:58pm"
        )
    }

    @Test
    fun mapsLongRunActivity() {
        Mapper.mapActivityRow(apiModel(workout_type = 2)) shouldBe ActivityCard(
            id = 123L,
            name = "my activity",
            type = "Run",
            label = "Long",
            distance = "10.1k",
            duration = "40:00",
            start = "Tuesday 25 Oct @ 5:58pm"
        )
    }

    @Test
    fun mapsWorkoutActivity() {
        Mapper.mapActivityRow(apiModel(workout_type = 3)) shouldBe ActivityCard(
            id = 123L,
            name = "my activity",
            type = "Run",
            label = "Workout",
            distance = "10.1k",
            duration = "40:00",
            start = "Tuesday 25 Oct @ 5:58pm"
        )
    }
}
