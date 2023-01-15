package com.jdamcd.runlog.shared.database

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AthleteDaoTest {

    private lateinit var dao: AthleteDao

    @BeforeTest
    fun setUp() {
        dao = AthleteDao(RunLogDB(testDbDriver()))
    }

    @Test
    fun insertsUser() {
        dao.insert(athlete(), runStats())

        val user = dao.user()
        user?.id shouldBe 123L
    }

    @Test
    fun updatesUser() {
        dao.insert(athlete(), runStats())
        dao.user()?.imageUrl shouldBe null

        dao.insert(athlete(imageUrl = "image.url"), runStats())
        dao.user()?.imageUrl shouldBe "image.url"
    }

    @Test
    fun userFlowEmitsRecord() = runTest {
        dao.insert(athlete(), runStats())

        val user = dao.userFlow().first()
        user?.id shouldBe 123L
    }

    @Test
    fun userIsNullForNoRecord() {
        dao.user() shouldBe null
    }

    @Test
    fun userFlowEmitsNullforNoRecord() = runTest {
        dao.userFlow().first() shouldBe null
    }
}
