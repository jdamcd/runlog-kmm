package com.jdamcd.runlog.shared.internal

import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.ActivityDetails
import com.jdamcd.runlog.shared.ActivitySubtype
import com.jdamcd.runlog.shared.ActivityType
import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.AthleteStats
import com.jdamcd.runlog.shared.KmSplits
import com.jdamcd.runlog.shared.Split
import io.kotest.matchers.shouldBe
import kotlin.test.BeforeTest
import kotlin.test.Test

class MapperTest {

    private lateinit var mapper: Mapper

    @BeforeTest
    fun setUp() {
        mapper = Mapper()
    }

    @Test
    fun mapsDefaultActivity() {
        mapper.mapActivityCard(activityModel()) shouldBe ActivityCard(
            id = 123L,
            name = "my activity",
            type = ActivityType.RUN,
            subtype = ActivitySubtype.DEFAULT,
            distance = "10.1k",
            duration = "40:00",
            pace = "3:58 /km",
            start = "TUESDAY 25 OCT @ 5:58PM",
            mapUrl = null
        )
    }

    @Test
    fun mapsRaceActivityUsingElapsedTime() {
        mapper.mapActivityCard(activityModel(workout_type = 1)) shouldBe ActivityCard(
            id = 123L,
            name = "my activity",
            type = ActivityType.RUN,
            subtype = ActivitySubtype.RACE,
            distance = "10.1k",
            duration = "41:00",
            pace = "4:04 /km",
            start = "TUESDAY 25 OCT @ 5:58PM",
            mapUrl = null
        )
    }

    @Test
    fun mapsLongRunActivity() {
        mapper.mapActivityCard(activityModel(workout_type = 2)) shouldBe ActivityCard(
            id = 123L,
            name = "my activity",
            type = ActivityType.RUN,
            subtype = ActivitySubtype.LONG,
            distance = "10.1k",
            duration = "40:00",
            pace = "3:58 /km",
            start = "TUESDAY 25 OCT @ 5:58PM",
            mapUrl = null
        )
    }

    @Test
    fun mapsWorkoutActivity() {
        mapper.mapActivityCard(activityModel(workout_type = 3)) shouldBe ActivityCard(
            id = 123L,
            name = "my activity",
            type = ActivityType.RUN,
            subtype = ActivitySubtype.WORKOUT,
            distance = "10.1k",
            duration = "40:00",
            pace = "3:58 /km",
            start = "TUESDAY 25 OCT @ 5:58PM",
            mapUrl = null
        )
    }

    @Test
    fun mapsDetailedActivity() {
        mapper.mapActivityDetails(detailedActivityModel()) shouldBe ActivityDetails(
            id = 123L,
            name = "my activity",
            type = ActivityType.RUN,
            subtype = ActivitySubtype.LONG,
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
            pace = "3:58 /km",
            start = "TUESDAY 25 OCT @ 5:58PM",
            mapUrl = null,
            splitsInfo = KmSplits(
                splits = listOf(
                    Split(
                        number = 1,
                        distance = "1k",
                        elapsedDuration = "6:00",
                        movingDuration = "5:00",
                        elevation = 10,
                        averageHeartrate = 160,
                        pace = "5:00",
                        paceSeconds = 300,
                        paceZone = 2
                    )
                ),
                minSeconds = 300,
                maxSeconds = 300,
                hasHeartrate = true
            )
        )
    }

    @Test
    fun mapsAthleteAndStatsToProfile() {
        mapper.mapProfile(athleteModel(), athleteStatsModel()) shouldBe AthleteProfile(
            id = 123L,
            username = "jdamcd",
            name = "Jamie McDonald",
            imageUrl = "image.url",
            recentRuns = AthleteStats(
                distance = "100k",
                pace = "5:33 /km"
            ),
            yearRuns = AthleteStats(
                distance = "1,000k",
                pace = "5:33 /km"
            ),
            allRuns = AthleteStats(
                distance = "5,000k",
                pace = "5:33 /km"
            )
        )
    }
}
