package com.jdamcd.runlog.android.training

import app.cash.turbine.test
import com.jdamcd.runlog.android.util.TestCoroutinesRule
import com.jdamcd.runlog.android.util.activityCard1
import com.jdamcd.runlog.android.util.activityCard2
import com.jdamcd.runlog.shared.Result
import com.jdamcd.runlog.shared.Strava
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class TrainingViewModelTest {

    @get:Rule val coroutineRule = TestCoroutinesRule()

    private val strava: Strava = mock()

    private lateinit var viewModel: TrainingViewModel

    @Before
    fun setUp() {
        viewModel = TrainingViewModel(strava)
    }

    @Test
    fun `load success emits loading then data`() = runTest {
        val activities = listOf(activityCard1)
        whenever(strava.activities()).thenReturn(Result.Data(activities))

        viewModel.flow.test {
            viewModel.load()

            awaitItem() shouldBe TrainingState.Loading
            awaitItem() shouldBe TrainingState.Data(activities)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `load failure emits loading then error`() = runTest {
        whenever(strava.activities()).thenReturn(Result.Error(Throwable(), recoverable = true))

        viewModel.flow.test {
            viewModel.load()

            awaitItem() shouldBe TrainingState.Loading
            awaitItem() shouldBe TrainingState.Error(recoverable = true)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `refresh replays current data then update`() = runTest {
        val load = listOf(activityCard1)
        val refresh = listOf(activityCard1, activityCard2)
        whenever(strava.activities()).thenReturn(Result.Data(load), Result.Data(refresh))

        viewModel.flow.test {
            viewModel.load()

            awaitItem() shouldBe TrainingState.Loading
            awaitItem() shouldBe TrainingState.Data(load)

            viewModel.refresh()

            awaitItem() shouldBe TrainingState.Refreshing(load)
            awaitItem() shouldBe TrainingState.Data(refresh)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `refresh emits loading if no current data`() = runTest {
        val activities = listOf(activityCard1)
        whenever(strava.activities()).thenReturn(Result.Data(activities))

        viewModel.flow.test {
            viewModel.refresh()

            awaitItem() shouldBe TrainingState.Loading
            awaitItem() shouldBe TrainingState.Data(activities)

            cancelAndConsumeRemainingEvents()
        }
    }
}
