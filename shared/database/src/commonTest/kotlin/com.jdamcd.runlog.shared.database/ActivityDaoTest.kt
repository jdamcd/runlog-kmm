package com.jdamcd.runlog.shared.database

import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
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
        dao.insert(listOf(activity()))

        dao.allActivities() shouldBe listOf(activity())
    }

    @Test
    fun `inserts multiple activities`() {
        val activity1 = activity(id = 123L)
        val activity2 = activity(id = 456L)

        dao.insert(listOf(activity1, activity2))

        dao.allActivities() shouldBe listOf(activity1, activity2)
    }

    @Test
    fun `insert updates activity by ID if already stored`() {
        val activity1 = activity(id = 123L)
        val activity2 = activity(id = 456L)
        val activity3 = activity(id = 123L, isPrivate = true)

        dao.insert(listOf(activity1, activity2))
        dao.insert(listOf(activity3))

        dao.allActivities() shouldContainExactlyInAnyOrder listOf(activity2, activity3)
    }

    @Test
    fun `deletes all activities`() {
        dao.insert(listOf(activity()))

        dao.clear()

        dao.allActivities() shouldBe emptyList()
    }
}
