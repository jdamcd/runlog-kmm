package com.jdamcd.runlog.shared.internal

import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.AthleteStats
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
