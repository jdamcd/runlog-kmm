package com.jdamcd.runlog.shared.database

import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DatabaseUtilTest {

    private lateinit var dbUtil: DatabaseUtil

    private val db = RunLogDB(testDbDriver())
    private val athleteDao = SqlAthleteDao(db)
    private var activityDao = SqlActivityDao(db)

    @BeforeTest
    fun setUp() {
        dbUtil = DatabaseUtil(athleteDao, activityDao)
    }

    @Test
    fun `clears all data`() = runTest {
        athleteDao.insert(athlete(), runStats())
        activityDao.insert(listOf(activity()))

        dbUtil.clear()

        athleteDao.user() shouldBe null
        activityDao.allActivities() shouldBe emptyList()
    }
}
