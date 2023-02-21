package com.jdamcd.runlog.android.training

import app.cash.turbine.test
import com.jdamcd.runlog.android.util.TestCoroutinesRule
import com.jdamcd.runlog.android.util.activityCard1
import com.jdamcd.runlog.android.util.activityCard2
import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.StravaActivity
import com.jdamcd.runlog.shared.StravaProfile
import com.jdamcd.runlog.shared.util.RefreshState
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TrainingViewModelTest {

    @get:Rule val coroutineRule = TestCoroutinesRule()

    private val stravaActivity: StravaActivity = mockk()
    private val stravaProfile: StravaProfile = mockk()

    private lateinit var flow: MutableSharedFlow<List<ActivityCard>>

    @Before
    fun setup() {
        coEvery { stravaProfile.userImageUrl() } returns null
        flow = MutableSharedFlow()
        coEvery { stravaActivity.refresh() } returns RefreshState.LOADING
        every { stravaActivity.activitiesFlow() } returns flow
    }

    @Test
    fun `emits initial loading state`() = runTest {
        val viewModel = TrainingViewModel(stravaActivity, stravaProfile)

        viewModel.contentFlow.test {
            flow.emit(emptyList())
            awaitItem() shouldBe TrainingState.Loading
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `load success emits loading then data`() = runTest {
        val activities = listOf(activityCard1, activityCard2)
        coEvery { stravaActivity.refresh() } returns RefreshState.SUCCESS

        val viewModel = TrainingViewModel(stravaActivity, stravaProfile)

        viewModel.contentFlow.test {
            flow.emit(emptyList())
            awaitItem() shouldBe TrainingState.Loading
            flow.emit(activities)
            awaitItem() shouldBe TrainingState.Data(activities)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `load emits profile image URL for status bar`() = runTest {
        val imageUrl = "image.url/123"
        coEvery { stravaProfile.userImageUrl() } returns imageUrl

        val viewModel = TrainingViewModel(stravaActivity, stravaProfile)

        viewModel.statusFlow.test {
            awaitItem() shouldBe StatusBarState.ProfileImage(imageUrl)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `load emits empty profile image state for status bar`() = runTest {
        coEvery { stravaProfile.userImageUrl() } returns null

        val viewModel = TrainingViewModel(stravaActivity, stravaProfile)

        viewModel.statusFlow.test {
            awaitItem() shouldBe StatusBarState.NoProfileImage
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `load failure emits error`() = runTest {
        coEvery { stravaActivity.refresh() } returns RefreshState.ERROR

        val viewModel = TrainingViewModel(stravaActivity, stravaProfile)

        viewModel.contentFlow.test {
            awaitItem() shouldBe TrainingState.Loading
            flow.emit(emptyList())
            awaitItem() shouldBe TrainingState.Error
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `refresh replays current data then updates`() = runTest {
        val load = listOf(activityCard1)
        val refresh = listOf(activityCard1, activityCard2)
        coEvery { stravaActivity.refresh() } returns RefreshState.SUCCESS

        val viewModel = TrainingViewModel(stravaActivity, stravaProfile)

        viewModel.contentFlow.test {
            awaitItem() shouldBe TrainingState.Loading
            flow.emit(load)
            awaitItem() shouldBe TrainingState.Data(load)

            viewModel.refresh()

            awaitItem() shouldBe TrainingState.Refreshing(load)
            awaitItem() shouldBe TrainingState.Data(load)
            flow.emit(refresh)
            awaitItem() shouldBe TrainingState.Data(refresh)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `refresh emits loading if no current data`() = runTest {
        coEvery { stravaActivity.refresh() } returns RefreshState.LOADING

        val viewModel = TrainingViewModel(stravaActivity, stravaProfile)

        viewModel.contentFlow.test {
            viewModel.refresh()
            flow.emit(emptyList())
            awaitItem() shouldBe TrainingState.Loading
            cancelAndConsumeRemainingEvents()
        }
    }
}
