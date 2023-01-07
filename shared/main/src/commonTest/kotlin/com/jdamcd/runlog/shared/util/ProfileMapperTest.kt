package com.jdamcd.runlog.shared.util

import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.AthleteStats
import com.jdamcd.runlog.shared.profile.ProfileMapper
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
    fun mapsApiDetailedAthleteToDbModel() {
        mapper.athleteToDb(athleteModel(), isUser = true) shouldBe Athlete(
            id = 123L,
            username = "jdamcd",
            name = "Jamie McDonald",
            imageUrl = "image.url",
            isUser = true
        )
    }

    @Test
    fun mapsApiActivityStatsToDbModel() {
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
    fun mapsAthleteWithStatsToUiModel() {
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

    // TODO: Move to DB module
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
