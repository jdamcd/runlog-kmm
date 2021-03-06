package com.jdamcd.runlog.android.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.jdamcd.runlog.android.util.TestCoroutinesRule
import com.jdamcd.runlog.android.util.TestLifecycleOwner
import com.jdamcd.runlog.android.util.activityCard
import com.jdamcd.runlog.shared.Result
import com.jdamcd.runlog.shared.Strava
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule val coroutineRule = TestCoroutinesRule()
    @get:Rule val executorRule = InstantTaskExecutorRule()

    private val observer: Observer<ActivityFeedState> = mock()
    private val strava: Strava = mock()

    private lateinit var lifecycleOwner: TestLifecycleOwner
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        lifecycleOwner = TestLifecycleOwner().apply { onCreate() }
        viewModel = MainViewModel(strava)
        viewModel.uiModel.observe(lifecycleOwner, observer)
    }

    @Test
    fun `load success emits loading then data`() {
        coroutineRule.testDispatcher.runBlockingTest {
            lifecycleOwner.onResume()
            val activities = listOf(activityCard)
            whenever(strava.activities()).thenReturn(Result.Data(activities))

            viewModel.load()

            verify(strava).activities()
            inOrder(observer) {
                verify(observer).onChanged(ActivityFeedState.Loading)
                verify(observer).onChanged(ActivityFeedState.Data(activities))
            }
        }
    }

    @Test
    fun `load failure emits loading then error`() {
        coroutineRule.testDispatcher.runBlockingTest {
            lifecycleOwner.onResume()
            whenever(strava.activities()).thenReturn(Result.Error(Throwable(), recoverable = true))

            viewModel.load()

            verify(strava).activities()
            inOrder(observer) {
                verify(observer).onChanged(ActivityFeedState.Loading)
                verify(observer).onChanged(ActivityFeedState.Error(recoverable = true))
            }
        }
    }

    @After
    fun tearDown() {
        lifecycleOwner.onDestroy()
    }
}
