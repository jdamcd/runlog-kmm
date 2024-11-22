package com.jdamcd.runlog.android.training

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.jdamcd.runlog.android.util.TestCoroutinesRule
import com.jdamcd.runlog.shared.ActivityDetails
import com.jdamcd.runlog.shared.StravaActivity
import com.jdamcd.runlog.shared.util.Result
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ActivityViewModelTest {

    @get:Rule val coroutineRule = TestCoroutinesRule()

    private val stravaActivity: StravaActivity = mockk()
    private val activityDetails: ActivityDetails = mockk()

    private val savedState = SavedStateHandle(initialState = mapOf("id" to 123L))

    @Test
    fun `emits initial loading state`() = runTest {
        coEvery { stravaActivity.activityDetails(123L) } returns Result.Empty

        val viewModel = ActivityViewModel(savedState, stravaActivity)

        viewModel.flow.test {
            awaitItem() shouldBe ActivityState.Loading
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `load success emits data`() = runTest {
        coEvery { stravaActivity.activityDetails(123L) } returns Result.Data(activityDetails)

        val viewModel = ActivityViewModel(savedState, stravaActivity)

        viewModel.flow.test {
            awaitItem() shouldBe ActivityState.Data(activityDetails)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `load failure emits error`() = runTest {
        coEvery { stravaActivity.activityDetails(123L) } returns Result.Error(Exception())

        val viewModel = ActivityViewModel(savedState, stravaActivity)

        viewModel.flow.test {
            awaitItem() shouldBe ActivityState.Error
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `generateLink forwards ID to make URL`() {
        every { stravaActivity.linkUrl(123L) } returns "url/123"
        coEvery { stravaActivity.activityDetails(123L) } returns Result.Empty

        ActivityViewModel(savedState, stravaActivity).activityWebLink()

        verify { stravaActivity.linkUrl(123L) }
    }
}
