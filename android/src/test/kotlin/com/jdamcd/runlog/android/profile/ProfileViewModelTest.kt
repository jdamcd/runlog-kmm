package com.jdamcd.runlog.android.profile

import app.cash.turbine.test
import com.jdamcd.runlog.android.util.TestCoroutinesRule
import com.jdamcd.runlog.android.util.athleteProfile
import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.StravaProfile
import com.jdamcd.runlog.shared.util.RefreshState
import com.jdamcd.runlog.shared.util.Result
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProfileViewModelTest {

    @get:Rule val coroutineRule = TestCoroutinesRule()

    private val stravaProfile: StravaProfile = mockk()
    private val profile = athleteProfile

    @Test
    fun `emits initial loading state`() = runTest {
        every { stravaProfile.profileFlow() } returns MutableSharedFlow()
        coEvery { stravaProfile.refresh() } returns RefreshState.LOADING

        val viewModel = ProfileViewModel(stravaProfile)

        viewModel.flow.test {
            awaitItem() shouldBe ProfileState.Loading
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `emits loading then data`() = runTest {
        val profileFlow = MutableSharedFlow<Result<AthleteProfile>>()
        every { stravaProfile.profileFlow() } returns profileFlow
        coEvery { stravaProfile.refresh() } returns RefreshState.LOADING

        val viewModel = ProfileViewModel(stravaProfile)

        viewModel.flow.test {
            awaitItem() shouldBe ProfileState.Loading
            profileFlow.emit(Result.Data(profile))
            awaitItem() shouldBe ProfileState.Data(profile)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `emits loading then error if empty and refresh complete`() = runTest {
        val profileFlow = MutableSharedFlow<Result<AthleteProfile>>()
        every { stravaProfile.profileFlow() } returns profileFlow
        coEvery { stravaProfile.refresh() } returns RefreshState.ERROR

        val viewModel = ProfileViewModel(stravaProfile)

        viewModel.flow.test {
            awaitItem() shouldBe ProfileState.Loading
            profileFlow.emit(Result.Empty)
            awaitItem() shouldBe ProfileState.Error
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `keeps loading state if empty but refreshing`() = runTest {
        val profileFlow = MutableSharedFlow<Result<AthleteProfile>>()
        every { stravaProfile.profileFlow() } returns profileFlow
        coEvery { stravaProfile.refresh() } returns RefreshState.LOADING

        val viewModel = ProfileViewModel(stravaProfile)

        viewModel.flow.test {
            profileFlow.emit(Result.Empty)
            awaitItem() shouldBe ProfileState.Loading
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `refreshes on init`() = runTest {
        every { stravaProfile.profileFlow() } returns MutableSharedFlow()
        coEvery { stravaProfile.refresh() } returns RefreshState.SUCCESS

        ProfileViewModel(stravaProfile)

        coVerify { stravaProfile.refresh() }
    }

    @Test
    fun `calling refresh triggers new refresh`() = runTest {
        every { stravaProfile.profileFlow() } returns MutableSharedFlow()
        coEvery { stravaProfile.refresh() } returns RefreshState.SUCCESS

        ProfileViewModel(stravaProfile).refresh()

        coVerify(exactly = 2) { stravaProfile.refresh() }
    }
}
