package com.jdamcd.runlog.shared.database

import comjdamcdrunlogshareddatabase.Athlete
import comjdamcdrunlogshareddatabase.RunStats
import io.kotest.matchers.shouldBe
import kotlin.test.BeforeTest
import kotlin.test.Test

class AthleteDaoTest {

    private lateinit var dao: AthleteDao

    @BeforeTest
    fun setUp() {
        dao = AthleteDao(RunLogDB(testDbDriver()))
    }

    @Test
    fun testInsertUser() {
        dao.insert(athlete(), runStats())

        val user = dao.getUser()
        user.id shouldBe 123L
    }

    @Test
    fun testUpdateUser() {
        dao.insert(athlete(), runStats())
        dao.getUser().imageUrl shouldBe null

        dao.insert(athlete(imageUrl = "image.url"), runStats())
        dao.getUser().imageUrl shouldBe "image.url"
    }

    private fun athlete(imageUrl: String? = null) = Athlete(
        id = 123L,
        username = "jdamcd",
        name = "Jamie McDonald",
        imageUrl = imageUrl,
        isUser = true
    )

    private fun runStats() = RunStats(
        id = 123L,
        recentDistance = 100_000.0f,
        recentPace = 333,
        yearDistance = 1_000_000.0f,
        yearPace = 333,
        allDistance = 5_000_000.0f,
        allPace = 333
    )
}
