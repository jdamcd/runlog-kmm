package com.jdamcd.runlog.shared.profile

import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.AthleteStats
import com.jdamcd.runlog.shared.util.athleteModel
import com.jdamcd.runlog.shared.util.athleteStatsModel
import comjdamcdrunlogshareddatabase.Athlete
import comjdamcdrunlogshareddatabase.AthleteWithStats
import comjdamcdrunlogshareddatabase.RunStats
import io.kotest.matchers.shouldBe
import kotlin.test.BeforeTest
import kotlin.test.Test

class ProfileMapperTest {

    private lateinit var mapper: ProfileMapper

    @BeforeTest
    fun setUp() {
        mapper = ProfileMapper()
    }

    @Test
    fun `maps ApiDetailedAthlete to DB model`() {
        mapper.athleteToDb(athleteModel(), isUser = true) shouldBe Athlete(
            id = 123L,
            username = "jdamcd",
            name = "Jamie McDonald",
            imageUrl = "image.url",
            isUser = true
        )
    }

    @Test
    fun `maps ApiActivityStats to DB model`() {
        mapper.runStatsToDb(id = 123L, athleteStatsModel()) shouldBe RunStats(
            id = 123L,
            recentDistance = 100_000.0f,
            recentPace = 333,
            yearDistance = 1_000_000.0f,
            yearPace = 333,
            allDistance = 5_000_000.0f,
            allPace = 333
        )
    }

    @Test
    fun `maps AthleteWithStats to UI model`() {
        mapper.dbToUi(athleteDbModel()) shouldBe AthleteProfile(
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

    private fun athleteDbModel() = AthleteWithStats(
        id = 123L,
        username = "jdamcd",
        name = "Jamie McDonald",
        imageUrl = "image.url",
        isUser = true,
        id_ = 123L,
        recentDistance = 100_000.0f,
        recentPace = 333,
        yearDistance = 1_000_000.0f,
        yearPace = 333,
        allDistance = 5_000_000.0f,
        allPace = 333
    )
}
