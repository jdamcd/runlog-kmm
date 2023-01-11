package com.jdamcd.runlog.android.profile

import app.cash.turbine.test
import com.jdamcd.runlog.android.util.TestCoroutinesRule
import com.jdamcd.runlog.android.util.athleteProfile
import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.StravaProfile
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class ProfileViewModelTest {

    @get:Rule val coroutineRule = TestCoroutinesRule()

    private val stravaProfile: StravaProfile = mock()

    @Test
    fun `load success emits loading then data`() = runTest {
        val profile = athleteProfile
        val upstreamFlow = MutableSharedFlow<AthleteProfile>()
        whenever(stravaProfile.profileFlow()).thenReturn(upstreamFlow)

        val viewModel = ProfileViewModel(stravaProfile)

        viewModel.flow.test {
            awaitItem() shouldBe ProfileState.Loading
            upstreamFlow.emit(profile)
            awaitItem() shouldBe ProfileState.Data(profile)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `refresh forwards to StravaProfile`() {
        val viewModel = ProfileViewModel(stravaProfile)

        viewModel.refresh()

        verify(stravaProfile).refresh()
    }
}
