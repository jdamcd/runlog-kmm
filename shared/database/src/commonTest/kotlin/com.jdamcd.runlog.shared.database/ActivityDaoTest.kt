package com.jdamcd.runlog.shared.database

import io.kotest.matchers.shouldBe
import kotlin.test.BeforeTest
import kotlin.test.Test

class ActivityDaoTest {

    private lateinit var dao: SqlActivityDao

    @BeforeTest
    fun setUp() {
        dao = SqlActivityDao(RunLogDB(testDbDriver()))
        dao.clear()
    }

    @Test
    fun `inserts activity`() {
        dao.insert(activity())

        dao.allActivities() shouldBe listOf(activity())
    }

    @Test
    fun `inserts multiple activities`() {
        val activity1 = activity(id = 123L)
        val activity2 = activity(id = 456L)

        dao.insert(activity1)
        dao.insert(activity2)

        dao.allActivities() shouldBe listOf(activity1, activity2)
    }

    @Test
    fun `deletes all activities`() {
        dao.insert(activity())

        dao.clear()

        dao.allActivities() shouldBe emptyList()
    }
}
