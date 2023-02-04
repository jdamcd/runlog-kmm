package com.jdamcd.runlog.android.training

import app.cash.turbine.test
import com.jdamcd.runlog.android.util.TestCoroutinesRule
import com.jdamcd.runlog.android.util.activityCard1
import com.jdamcd.runlog.android.util.activityCard2
import com.jdamcd.runlog.shared.StravaActivity
import com.jdamcd.runlog.shared.StravaProfile
import com.jdamcd.runlog.shared.util.Result
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TrainingViewModelTest {

    @get:Rule val coroutineRule = TestCoroutinesRule()

    private val stravaActivity: StravaActivity = mockk()
    private val stravaProfile: StravaProfile = mockk()

    private lateinit var viewModel: TrainingViewModel

    @Before
    fun setUp() {
        viewModel = TrainingViewModel(stravaActivity, stravaProfile)
    }

    @Test
    fun `load success emits loading then data`() = runTest {
        val activities = listOf(activityCard1)
        coEvery { stravaActivity.activities() } returns Result.Data(activities)

        viewModel.contentFlow.test {
            viewModel.load()

            awaitItem() shouldBe TrainingState.Loading
            awaitItem() shouldBe TrainingState.Data(activities)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `load emits profile image URL for status bar`() = runTest {
        val imageUrl = "image.url/123"
        coEvery { stravaProfile.userImageUrl() } returns imageUrl
        coEvery { stravaActivity.activities() } returns Result.Error(Throwable())

        viewModel.statusFlow.test {
            viewModel.load()

            awaitItem() shouldBe StatusBarState.NoProfileImage
            awaitItem() shouldBe StatusBarState.ProfileImage(imageUrl)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `load failure emits loading then error`() = runTest {
        coEvery { stravaActivity.activities() } returns Result.Error(Throwable())

        viewModel.contentFlow.test {
            viewModel.load()

            awaitItem() shouldBe TrainingState.Loading
            awaitItem() shouldBe TrainingState.Error
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `refresh replays current data then update`() = runTest {
        val load = listOf(activityCard1)
        val refresh = listOf(activityCard1, activityCard2)
        coEvery { stravaActivity.activities() } returns Result.Data(load) andThen Result.Data(refresh)

        viewModel.contentFlow.test {
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
        coEvery { stravaActivity.activities() } returns Result.Data(activities)

        viewModel.contentFlow.test {
            viewModel.refresh()

            awaitItem() shouldBe TrainingState.Loading
            awaitItem() shouldBe TrainingState.Data(activities)

            cancelAndConsumeRemainingEvents()
        }
    }
}
