package com.jdamcd.runlog.shared.internal

import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.ActivityType
import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.AthleteStats
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class MapperTest {

    @Test
    fun mapsDefaultActivity() {
        Mapper.mapActivityRow(activityModel()) shouldBe ActivityCard(
            id = 123L,
            name = "my activity",
            type = ActivityType.RUN,
            isRace = false,
            distance = "10.1k",
            duration = "40:00",
            pace = "3:58/k",
            start = "TUESDAY 25 OCT @ 5:58PM",
            mapUrl = null
        )
    }

    @Test
    fun mapsRaceActivityUsingElapsedTime() {
        Mapper.mapActivityRow(activityModel(workout_type = 1)) shouldBe ActivityCard(
            id = 123L,
            name = "my activity",
            type = ActivityType.RUN,
            isRace = true,
            distance = "10.1k",
            duration = "41:00",
            pace = "4:04/k",
            start = "TUESDAY 25 OCT @ 5:58PM",
            mapUrl = null
        )
    }

    @Test
    fun mapsLongRunActivity() {
        Mapper.mapActivityRow(activityModel(workout_type = 2)) shouldBe ActivityCard(
            id = 123L,
            name = "my activity",
            type = ActivityType.RUN,
            isRace = false,
            distance = "10.1k",
            duration = "40:00",
            pace = "3:58/k",
            start = "TUESDAY 25 OCT @ 5:58PM",
            mapUrl = null
        )
    }

    @Test
    fun mapsWorkoutActivity() {
        Mapper.mapActivityRow(activityModel(workout_type = 3)) shouldBe ActivityCard(
            id = 123L,
            name = "my activity",
            type = ActivityType.RUN,
            isRace = false,
            distance = "10.1k",
            duration = "40:00",
            pace = "3:58/k",
            start = "TUESDAY 25 OCT @ 5:58PM",
            mapUrl = null
        )
    }

    @Test
    fun mapsAthleteAndStatsToProfile() {
        Mapper.mapProfile(athleteModel(), athleteStatsModel()) shouldBe AthleteProfile(
            id = 123L,
            username = "jdamcd",
            name = "Jamie McDonald",
            imageUrl = "image.url",
            recentRuns = AthleteStats(
                distance = "100k",
                pace = "5:33/k"
            ),
            yearRuns = AthleteStats(
                distance = "1,000k",
                pace = "5:33/k"
            ),
            allRuns = AthleteStats(
                distance = "5,000k",
                pace = "5:33/k"
            ),
        )
    }
}
