package com.jdamcd.runlog.shared.activity

import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.ActivityDetails
import com.jdamcd.runlog.shared.ActivitySubtype
import com.jdamcd.runlog.shared.ActivityType
import com.jdamcd.runlog.shared.Split
import com.jdamcd.runlog.shared.testutil.MockClock
import com.jdamcd.runlog.shared.testutil.activityModel
import com.jdamcd.runlog.shared.testutil.detailedActivityModel
import comjdamcdrunlogshareddatabase.Activity
import io.kotest.matchers.shouldBe
import kotlin.test.BeforeTest
import kotlin.test.Test

class ActivityMapperTest {

    private lateinit var mapper: ActivityMapper
    private val clock = MockClock

    @BeforeTest
    fun setUp() {
        clock.epochSeconds = 456L
        mapper = ActivityMapper(MockClock)
    }

    @Test
    fun `maps default ApiSummaryActivity to DB model`() {
        mapper.summaryApiToDb(activityModel()) shouldBe Activity(
            id = 123L,
            name = "my activity",
            isPrivate = false,
            type = "RUN",
            subtype = "DEFAULT",
            distance = 10_100.0f,
            duration = 2400, // moving time
            pace = 238, // moving pace
            start = "2022-10-25T17:58:50Z",
            mapPolyline = "abc",
            lastUpdated = 456L
        )
    }

    @Test
    fun `maps race ApiSummaryActivity to DB model`() {
        mapper.summaryApiToDb(activityModel(workout_type = 1)) shouldBe Activity(
            id = 123L,
            name = "my activity",
            isPrivate = false,
            type = "RUN",
            subtype = "RACE",
            distance = 10_100.0f,
            duration = 2460, // elapsed time
            pace = 244, // elapsed pace
            start = "2022-10-25T17:58:50Z",
            mapPolyline = "abc",
            lastUpdated = 456L
        )
    }

    @Test
    fun `maps ApiSummaryActivity with no map to DB model`() {
        mapper.summaryApiToDb(activityModel(map = null)) shouldBe Activity(
            id = 123L,
            name = "my activity",
            isPrivate = false,
            type = "RUN",
            subtype = "DEFAULT",
            distance = 10_100.0f,
            duration = 2400, // moving time
            pace = 238, // moving pace
            start = "2022-10-25T17:58:50Z",
            mapPolyline = null,
            lastUpdated = 456L
        )
    }

    @Test
    fun `maps DB Activity to UI model`() {
        val dbModel = mapper.summaryApiToDb(activityModel(map = null))
        mapper.summaryDbToUi(dbModel) shouldBe ActivityCard(
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
    fun `maps detailed activity`() {
        mapper.detailApiToUi(detailedActivityModel()) shouldBe ActivityDetails(
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
            splits = listOf(
                Split(
                    number = 1,
                    distance = "1",
                    isPartial = false,
                    elapsedDuration = "6:00",
                    movingDuration = "5:10",
                    elevation = "10",
                    averageHeartrate = "160",
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
                    elevation = "20",
                    averageHeartrate = "180",
                    pace = "5:00",
                    paceSeconds = 300,
                    paceZone = 2,
                    visualisation = 1.0f
                )
            )
        )
    }
}
