package com.jdamcd.runlog.android.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.jdamcd.runlog.android.training.TrainingState
import com.jdamcd.runlog.android.training.TrainingViewModel
import com.jdamcd.runlog.android.util.TestCoroutinesRule
import com.jdamcd.runlog.android.util.TestLifecycleOwner
import com.jdamcd.runlog.android.util.activityCard
import com.jdamcd.runlog.shared.Result
import com.jdamcd.runlog.shared.Strava
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class TrainingViewModelTest {

    @get:Rule val coroutineRule = TestCoroutinesRule()

    @get:Rule val executorRule = InstantTaskExecutorRule()

    private val observer: Observer<TrainingState> = mock()
    private val strava: Strava = mock()

    private lateinit var lifecycleOwner: TestLifecycleOwner
    private lateinit var viewModel: TrainingViewModel

    @Before
    fun setUp() {
        lifecycleOwner = TestLifecycleOwner().apply { onCreate() }
        viewModel = TrainingViewModel(strava)
        viewModel.uiModel.observe(lifecycleOwner, observer)
    }

    @Test
    fun `load success emits loading then data`() {
        runTest(coroutineRule.testDispatcher) {
            lifecycleOwner.onResume()
            val activities = listOf(activityCard)
            whenever(strava.activities()).thenReturn(Result.Data(activities))

            viewModel.load()

            verify(strava).activities()
            inOrder(observer) {
                verify(observer).onChanged(TrainingState.Loading)
                verify(observer).onChanged(TrainingState.Data(activities))
            }
        }
    }

    @Test
    fun `load failure emits loading then error`() {
        runTest(coroutineRule.testDispatcher) {
            lifecycleOwner.onResume()
            whenever(strava.activities()).thenReturn(Result.Error(Throwable(), recoverable = true))

            viewModel.load()

            verify(strava).activities()
            inOrder(observer) {
                verify(observer).onChanged(TrainingState.Loading)
                verify(observer).onChanged(TrainingState.Error(recoverable = true))
            }
        }
    }

    @After
    fun tearDown() {
        lifecycleOwner.onDestroy()
    }
}
