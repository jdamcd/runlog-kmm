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
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ActivityViewModelTest {

    @get:Rule
    val coroutineRule = TestCoroutinesRule()

    private val stravaActivity: StravaActivity = mockk()
    private val activityDetails: ActivityDetails = mockk()

    private lateinit var viewModel: ActivityViewModel

    @Before
    fun setUp() {
        val state = SavedStateHandle(initialState = mapOf("id" to 123L))
        viewModel = ActivityViewModel(state, stravaActivity)
    }

    @Test
    fun `load success emits loading then data`() = runTest {
        coEvery { stravaActivity.activityDetails(123L) } returns Result.Data(activityDetails)

        viewModel.flow.test {
            viewModel.load()

            awaitItem() shouldBe ActivityState.Loading
            awaitItem() shouldBe ActivityState.Data(activityDetails)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `load failure emits loading then error`() = runTest {
        coEvery { stravaActivity.activityDetails(123L) } returns Result.Error(Throwable())

        viewModel.flow.test {
            viewModel.load()

            awaitItem() shouldBe ActivityState.Loading
            awaitItem() shouldBe ActivityState.Error
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `generateLink forwards ID to make URL`() {
        every { stravaActivity.linkUrl(123L) } returns "url/123"

        viewModel.activityWebLink()

        verify { stravaActivity.linkUrl(123L) }
    }
}
