package com.jdamcd.runlog.shared.database

import comjdamcdrunlogshareddatabase.Athlete
import comjdamcdrunlogshareddatabase.AthleteWithStats
import comjdamcdrunlogshareddatabase.RunStats
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DatabaseUtilTest {

    private lateinit var dbUtil: DatabaseUtil

    private var athleteDao = MockAthleteDao()
    private var activityDao = MockActivityDao()

    @BeforeTest
    fun setUp() {
        dbUtil = DatabaseUtil(athleteDao, activityDao)
    }

    @Test
    fun `clears all data`() = runTest {
        dbUtil.clear()

        athleteDao.clearCalled shouldBe true
        activityDao.clearCalled shouldBe true
    }
}

class MockActivityDao : ActivityDao {

    var clearCalled = false

    override fun clear() {
        clearCalled = true
    }
}

class MockAthleteDao : AthleteDao {

    override fun insert(athlete: Athlete) {}
    override fun insert(athlete: Athlete, runStats: RunStats) {}
    override fun user(): AthleteWithStats? = null
    override fun userImageUrl(): String? = null
    override fun userFlow(): Flow<AthleteWithStats?> = MutableSharedFlow()

    var clearCalled = false

    override fun clear() {
        clearCalled = true
    }
}
