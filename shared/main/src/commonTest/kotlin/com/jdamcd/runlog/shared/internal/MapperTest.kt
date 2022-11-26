package com.jdamcd.runlog.shared.internal

import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.ActivityDetails
import com.jdamcd.runlog.shared.ActivityType
import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.AthleteStats
import com.jdamcd.runlog.shared.Split
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class MapperTest {

    @Test
    fun mapsDefaultActivity() {
        Mapper.mapActivityCard(activityModel()) shouldBe ActivityCard(
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
        Mapper.mapActivityCard(activityModel(workout_type = 1)) shouldBe ActivityCard(
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
        Mapper.mapActivityCard(activityModel(workout_type = 2)) shouldBe ActivityCard(
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
        Mapper.mapActivityCard(activityModel(workout_type = 3)) shouldBe ActivityCard(
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
    fun mapsDetailedActivity() {
        Mapper.mapActivityDetails(detailedActivityModel()) shouldBe ActivityDetails(
            id = 123L,
            name = "my activity",
            type = ActivityType.RUN,
            isRace = false,
            description = "my description",
            kudos = 0,
            distance = "10.1k",
            elapsedDuration = "41:00",
            movingDuration = "40:00",
            elevationGain = "10m",
            elevationLow = "5m",
            elevationHigh = "15m",
            effort = 50,
            calories = 200,
            averageHeartrate = 160,
            maxHeartrate = 180,
            pace = "3:58/k",
            start = "TUESDAY 25 OCT @ 5:58PM",
            mapUrl = null,
            splits = listOf(
                Split(
                    split = 1,
                    distance = "1k",
                    elapsedDuration = "5:00",
                    movingDuration = "5:00",
                    elevationGain = "10m",
                    averageHeartrate = 160,
                    pace = "5:00/k",
                    paceZone = 2
                )
            )
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
            )
        )
    }
}
