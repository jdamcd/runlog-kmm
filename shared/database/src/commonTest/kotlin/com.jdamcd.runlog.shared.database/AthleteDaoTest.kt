package com.jdamcd.runlog.shared.database

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
}
