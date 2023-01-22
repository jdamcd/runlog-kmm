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
        dao.clear()
    }

    @Test
    fun `inserts user`() {
        dao.insert(athlete(), runStats())

        val user = dao.user()
        user?.id shouldBe 123L
    }

    @Test
    fun `updates user`() {
        dao.insert(athlete(), runStats())
        dao.user()?.imageUrl shouldBe null

        dao.insert(athlete(imageUrl = "image.url"), runStats())
        dao.user()?.imageUrl shouldBe "image.url"
    }

    @Test
    fun `deletes all athlete data`() {
        dao.insert(athlete(), runStats())

        dao.clear()

        dao.user() shouldBe null
    }

    @Test
    fun `user flow emits stored athlete data`() = runTest {
        dao.insert(athlete(), runStats())

        val user = dao.userFlow().first()
        user?.id shouldBe 123L
    }

    @Test
    fun `user is null if not inserted`() {
        dao.user() shouldBe null
    }

    @Test
    fun `user flow emits null if not inserted`() = runTest {
        dao.userFlow().first() shouldBe null
    }

    @Test
    fun `user flow emits data inserted after null`() = runTest {
        val flow = dao.userFlow()

        flow.first() shouldBe null
        dao.insert(athlete(), runStats())

        val user = flow.first()
        user?.id shouldBe 123L
    }
}
