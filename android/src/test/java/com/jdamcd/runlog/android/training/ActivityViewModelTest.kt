package com.jdamcd.runlog.android.training

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.jdamcd.runlog.android.util.TestCoroutinesRule
import com.jdamcd.runlog.shared.ActivityDetails
import com.jdamcd.runlog.shared.Result
import com.jdamcd.runlog.shared.StravaActivity
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

    private val stravaActivity: StravaActivity = mock()
    private val activityDetails: ActivityDetails = mock()

    private lateinit var viewModel: ActivityViewModel

    @Before
    fun setUp() {
        val state = SavedStateHandle(initialState = mapOf("id" to 123L))
        viewModel = ActivityViewModel(state, stravaActivity)
    }

    @Test
    fun `load success emits loading then data`() = runTest {
        whenever(stravaActivity.activityDetails(123L)).thenReturn(Result.Data(activityDetails))

        viewModel.flow.test {
            viewModel.load()

            awaitItem() shouldBe ActivityState.Loading
            awaitItem() shouldBe ActivityState.Data(activityDetails)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `load failure emits loading then error`() = runTest {
        whenever(stravaActivity.activityDetails(123L)).thenReturn(Result.Error(Throwable(), recoverable = true))

        viewModel.flow.test {
            viewModel.load()

            awaitItem() shouldBe ActivityState.Loading
            awaitItem() shouldBe ActivityState.Error(recoverable = true)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `generateLink forwards ID to make URL`() {
        viewModel.activityWebLink()

        verify(stravaActivity).linkUrl(123L)
    }
}
