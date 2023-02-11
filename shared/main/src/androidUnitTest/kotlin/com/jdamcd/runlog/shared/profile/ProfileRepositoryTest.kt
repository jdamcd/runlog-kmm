package com.jdamcd.runlog.shared.profile

import app.cash.turbine.test
import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.api.StravaApi
import com.jdamcd.runlog.shared.database.AthleteDao
import com.jdamcd.runlog.shared.testutil.MockClock
import com.jdamcd.runlog.shared.testutil.MockLog
import com.jdamcd.runlog.shared.testutil.athleteDbModel
import com.jdamcd.runlog.shared.testutil.athleteModel
import com.jdamcd.runlog.shared.testutil.athleteStatsModel
import com.jdamcd.runlog.shared.util.RefreshState
import com.jdamcd.runlog.shared.util.Result
import comjdamcdrunlogshareddatabase.AthleteWithStats
import io.kotest.matchers.should
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
class ProfileRepositoryTest {

    private lateinit var repository: ProfileRepository

    private val api: StravaApi = mockk()
    private val dao: AthleteDao = mockk()
    private val mapper = AthleteMapper(MockClock)

    @Before
    fun setUp() {
        repository = ProfileRepository(api, dao, mapper, MockLog())
    }

    @Test
    fun `refresh success fetches athlete and saves to DB`() = runTest {
        coEvery { api.athlete() } returns athleteModel()
        coEvery { api.athleteStats(123L) } returns athleteStatsModel()
        every { dao.insert(any(), any()) } just runs

        repository.refresh() shouldBe RefreshState.SUCCESS
    }

    @Test
    fun `refresh returns error if request fails`() = runTest {
        coEvery { api.athlete() } returns athleteModel()
        coEvery { api.athleteStats(123L) } throws Exception()

        repository.refresh() shouldBe RefreshState.ERROR

        verify(exactly = 0) { dao.insert(any(), any()) }
    }

    @Test
    fun `refresh returns error if DB insert fails`() = runTest {
        coEvery { api.athlete() } returns athleteModel()
        coEvery { api.athleteStats(123L) } returns athleteStatsModel()
        every { dao.insert(any(), any()) } throws Exception()

        repository.refresh() shouldBe RefreshState.ERROR
    }

    @Test
    fun `profile returns user from DB`() = runTest {
        every { dao.user() } returns athleteDbModel()

        repository.profile() should beOfType<Result.Data<AthleteProfile>>()
    }

    @Test
    fun `profile returns empty if user is not in DB`() = runTest {
        every { dao.user() } returns null

        repository.profile() shouldBe Result.Empty
    }

    @Test
    fun `profile flow emits user from DB`() = runTest {
        val flow = MutableSharedFlow<AthleteWithStats?>()
        every { dao.userFlow() } returns flow

        repository.profileFlow().test {
            flow.emit(athleteDbModel())
            awaitItem() should beOfType<Result.Data<AthleteProfile>>()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `profile flow emits empty if user is not in DB`() = runTest {
        val flow = MutableSharedFlow<AthleteWithStats?>()
        every { dao.userFlow() } returns flow

        repository.profileFlow().test {
            flow.emit(null)
            awaitItem() shouldBe Result.Empty
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `userImageUrl returns string from DB`() = runTest {
        val url = "image.url/123"
        every { dao.userImageUrl() } returns url

        repository.userImageUrl() shouldBe url
    }

    @Test
    fun `userImageUrl returns null if not stored`() = runTest {
        every { dao.userImageUrl() } returns null

        repository.userImageUrl() shouldBe null
    }
}
