package com.jdamcd.runlog.android.profile

import app.cash.turbine.test
import com.jdamcd.runlog.android.util.TestCoroutinesRule
import com.jdamcd.runlog.android.util.athleteProfile
import com.jdamcd.runlog.shared.Result
import com.jdamcd.runlog.shared.StravaProfile
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class ProfileViewModelTest {

    @get:Rule val coroutineRule = TestCoroutinesRule()

    private val stravaProfile: StravaProfile = mock()

    private lateinit var viewModel: ProfileViewModel

    @Before
    fun setUp() {
        viewModel = ProfileViewModel(stravaProfile)
    }

    @Test
    fun `load success emits loading then data`() = runTest {
        val profile = athleteProfile
        whenever(stravaProfile.profile()).thenReturn(Result.Data(profile))

        viewModel.flow.test {
            viewModel.load()

            awaitItem() shouldBe ProfileState.Loading
            awaitItem() shouldBe ProfileState.Data(profile)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `load failure emits loading then error`() = runTest {
        whenever(stravaProfile.profile()).thenReturn(Result.Error(Throwable(), recoverable = true))

        viewModel.flow.test {
            viewModel.load()

            awaitItem() shouldBe ProfileState.Loading
            awaitItem() shouldBe ProfileState.Error(recoverable = true)
            cancelAndConsumeRemainingEvents()
        }
    }
}
