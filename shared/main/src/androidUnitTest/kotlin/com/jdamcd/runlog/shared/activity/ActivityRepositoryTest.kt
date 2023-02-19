package com.jdamcd.runlog.shared.activity

import app.cash.turbine.test
import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.api.StravaApi
import com.jdamcd.runlog.shared.database.ActivityDao
import com.jdamcd.runlog.shared.testutil.MockClock
import com.jdamcd.runlog.shared.testutil.MockLog
import com.jdamcd.runlog.shared.testutil.activityModel
import com.jdamcd.runlog.shared.util.RefreshState
import com.jdamcd.runlog.shared.util.Result
import comjdamcdrunlogshareddatabase.Activity
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beOfType
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ActivityRepositoryTest {

    private lateinit var repo: ActivityRepository

    private val api: StravaApi = mockk()
    private val dao: ActivityDao = mockk()
    private val mapper: ActivityMapper = ActivityMapper(MockClock)

    @Before
    fun setUp() {
        repo = ActivityRepository(api, dao, mapper, MockLog())
    }

    @Test
    fun `refresh success fetches activities and saves to DB`() = runTest {
        coEvery { api.activities() } returns listOf(activityModel())
        every { dao.insert(any()) } just runs

        repo.refresh() shouldBe RefreshState.SUCCESS
    }

    @Test
    fun `refresh returns error if request fails`() = runTest {
        coEvery { api.activities() } throws Exception()

        repo.refresh() shouldBe RefreshState.ERROR

        verify(exactly = 0) { dao.insert(any()) }
    }

    @Test
    fun `refresh returns error if DB insert fails`() = runTest {
        coEvery { api.activities() } returns listOf(activityModel())
        every { dao.insert(any()) } throws Exception()

        repo.refresh() shouldBe RefreshState.ERROR
    }

    @Test
    fun `activities returns activities from DB`() = runTest {
        val dbActivities = listOf(mapper.summaryApiToDb(activityModel()))
        every { dao.allActivities() } returns dbActivities

        repo.activities() shouldBe beOfType<Result.Data<List<ActivityCard>>>()
    }

    @Test
    fun `activities returns empty result if none stored`() = runTest {
        every { dao.allActivities() } returns emptyList()

        repo.activities() shouldBe Result.Empty
    }

    @Test
    fun `activitiesFlow emits activities from DB`() = runTest {
        val flow = MutableSharedFlow<List<Activity>>()
        val dbActivities = listOf(mapper.summaryApiToDb(activityModel()))
        every { dao.allActivitiesFlow() } returns flow

        repo.activitiesFlow().test {
            flow.emit(dbActivities)
            awaitItem() shouldBe beOfType<Result.Data<List<ActivityCard>>>()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `activitiesFlow emits empty result if none stored`() = runTest {
        val flow = MutableSharedFlow<List<Activity>>()
        every { dao.allActivitiesFlow() } returns flow

        repo.activitiesFlow().test {
            flow.emit(emptyList())
            awaitItem() shouldBe Result.Empty
            cancelAndConsumeRemainingEvents()
        }
    }
}
