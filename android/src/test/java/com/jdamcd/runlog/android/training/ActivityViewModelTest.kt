package com.jdamcd.runlog.android.training

import app.cash.turbine.test
import com.jdamcd.runlog.android.util.TestCoroutinesRule
import com.jdamcd.runlog.shared.ActivityDetails
import com.jdamcd.runlog.shared.Result
import com.jdamcd.runlog.shared.Strava
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class ActivityViewModelTest {

    @get:Rule
    val coroutineRule = TestCoroutinesRule()

    private val strava: Strava = mock()
    private val activityDetails: ActivityDetails = mock()

    private lateinit var viewModel: ActivityViewModel

    @Before
    fun setUp() {
        viewModel = ActivityViewModel(strava)
    }

    @Test
    fun `load success emits loading then data`() = runTest {
        whenever(strava.activityDetails(1L)).thenReturn(Result.Data(activityDetails))

        viewModel.flow.test {
            viewModel.load(1L)

            awaitItem() shouldBe ActivityState.Loading
            awaitItem() shouldBe ActivityState.Data(activityDetails)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `load failure emits loading then error`() = runTest {
        whenever(strava.activityDetails(2L)).thenReturn(Result.Error(Throwable(), recoverable = true))

        viewModel.flow.test {
            viewModel.load(2L)

            awaitItem() shouldBe ActivityState.Loading
            awaitItem() shouldBe ActivityState.Error(recoverable = true)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `generateLink forwards ID to make URL`() {
        viewModel.generateLink(3L)

        verify(strava).linkUrl(3L)
    }
}
