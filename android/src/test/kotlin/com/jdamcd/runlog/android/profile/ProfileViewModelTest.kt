package com.jdamcd.runlog.android.profile

import app.cash.turbine.test
import com.jdamcd.runlog.android.util.TestCoroutinesRule
import com.jdamcd.runlog.android.util.athleteProfile
import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.StravaProfile
import com.jdamcd.runlog.shared.util.RefreshState
import com.jdamcd.runlog.shared.util.Result
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class ProfileViewModelTest {

    @get:Rule val coroutineRule = TestCoroutinesRule()

    private val stravaProfile: StravaProfile = mock()
    private val profile = athleteProfile

    @Test
    fun `emits initial loading state`() = runTest {
        whenever(stravaProfile.profileFlow()).thenReturn(MutableSharedFlow())

        val viewModel = ProfileViewModel(stravaProfile)

        viewModel.flow.test {
            awaitItem() shouldBe ProfileState.Loading
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `emits loading then data`() = runTest {
        val upstreamFlow = MutableSharedFlow<Result<AthleteProfile>>()
        whenever(stravaProfile.profileFlow()).thenReturn(upstreamFlow)

        val viewModel = ProfileViewModel(stravaProfile)

        viewModel.flow.test {
            awaitItem() shouldBe ProfileState.Loading
            upstreamFlow.emit(Result.Data(profile))
            awaitItem() shouldBe ProfileState.Data(profile)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `emits loading then error if empty and refresh complete`() = runTest {
        val upstreamFlow = MutableSharedFlow<Result<AthleteProfile>>()
        whenever(stravaProfile.profileFlow()).thenReturn(upstreamFlow)
        whenever(stravaProfile.refresh()).thenReturn(RefreshState.ERROR)

        val viewModel = ProfileViewModel(stravaProfile)

        viewModel.flow.test {
            awaitItem() shouldBe ProfileState.Loading
            upstreamFlow.emit(Result.Empty)
            awaitItem() shouldBe ProfileState.Error
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `keeps loading state if empty but refreshing`() = runTest {
        val upstreamFlow = MutableSharedFlow<Result<AthleteProfile>>()
        whenever(stravaProfile.profileFlow()).thenReturn(upstreamFlow)
        whenever(stravaProfile.refresh()).thenReturn(RefreshState.LOADING)

        val viewModel = ProfileViewModel(stravaProfile)

        viewModel.flow.test {
            upstreamFlow.emit(Result.Empty)
            awaitItem() shouldBe ProfileState.Loading
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `refreshes on init`() = runTest {
        ProfileViewModel(stravaProfile)

        verify(stravaProfile).refresh()
    }

    @Test
    fun `calling refresh triggers new refresh`() = runTest {
        val viewModel = ProfileViewModel(stravaProfile)

        viewModel.refresh()

        verify(stravaProfile, times(2)).refresh()
    }
}
