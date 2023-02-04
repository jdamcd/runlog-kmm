package com.jdamcd.runlog.shared.activity

import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.ActivityDetails
import com.jdamcd.runlog.shared.ActivitySubtype
import com.jdamcd.runlog.shared.ActivityType
import com.jdamcd.runlog.shared.KmSplits
import com.jdamcd.runlog.shared.Split
import com.jdamcd.runlog.shared.util.activityModel
import com.jdamcd.runlog.shared.util.detailedActivityModel
import io.kotest.matchers.shouldBe
import kotlin.test.BeforeTest
import kotlin.test.Test

class ActivityMapperTest {

    private lateinit var mapper: ActivityMapper

    @BeforeTest
    fun setUp() {
        mapper = ActivityMapper()
    }

    @Test
    fun `maps default activity`() {
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
    fun `maps race activity using elapsed time`() {
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
    fun `maps long run activity`() {
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
    fun `maps workout activity`() {
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
    fun `maps detailed activity`() {
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
                        distance = "1",
                        isPartial = false,
                        elapsedDuration = "6:00",
                        movingDuration = "5:10",
                        elevation = 10,
                        averageHeartrate = 160,
                        pace = "5:10",
                        paceSeconds = 310,
                        paceZone = 2,
                        visualisation = 0.96666664f
                    ),
                    Split(
                        number = 2,
                        distance = "0.5",
                        isPartial = true,
                        elapsedDuration = "5:00",
                        movingDuration = "2:30",
                        elevation = 20,
                        averageHeartrate = 180,
                        pace = "5:00",
                        paceSeconds = 300,
                        paceZone = 2,
                        visualisation = 1.0f
                    )
                ),
                hasHeartrate = true,
                hasElevation = true
            )
        )
    }
}
