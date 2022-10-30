package com.jdamcd.runlog.shared.internal

import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.AthleteProfile
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class MapperTest {

    @Test
    fun mapsDefaultActivity() {
        Mapper.mapActivityRow(activityModel()) shouldBe ActivityCard(
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
        Mapper.mapActivityRow(activityModel(workout_type = 1)) shouldBe ActivityCard(
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
        Mapper.mapActivityRow(activityModel(workout_type = 2)) shouldBe ActivityCard(
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
        Mapper.mapActivityRow(activityModel(workout_type = 3)) shouldBe ActivityCard(
            id = 123L,
            name = "my activity",
            type = "Run",
            label = "Workout",
            distance = "10.1k",
            duration = "40:00",
            start = "Tuesday 25 Oct @ 5:58pm"
        )
    }

    @Test
    fun mapsAthleteAndStatsToProfile() {
        Mapper.mapProfile(athleteModel(), athleteStatsModel()) shouldBe AthleteProfile(
            id = 123L,
            username = "jdamcd",
            name = "Jamie McDonald",
            imageUrl = "image.url",
            fourWeekRunDistance = "100k",
            yearRunDistance = "1,000k",
            allTimeRunDistance = "5,000k"
        )
    }
}
