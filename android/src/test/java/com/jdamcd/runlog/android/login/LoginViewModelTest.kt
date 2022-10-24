package com.jdamcd.runlog.android.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.jdamcd.runlog.android.util.TestCoroutinesRule
import com.jdamcd.runlog.android.util.TestLifecycleOwner
import com.jdamcd.runlog.shared.LoginResult
import com.jdamcd.runlog.shared.Strava
import com.jdamcd.runlog.shared.UserState
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
class LoginViewModelTest {

    @get:Rule val coroutineRule = TestCoroutinesRule()

    @get:Rule val executorRule = InstantTaskExecutorRule()

    private val observer: Observer<LoginState> = mock()
    private val strava: Strava = mock()
    private val userState: UserState = mock()

    private lateinit var lifecycleOwner: TestLifecycleOwner
    private lateinit var viewModel: LoginViewModel

    private val code = "123"

    @Before
    fun setUp() {
        lifecycleOwner = TestLifecycleOwner().apply { onCreate() }
        viewModel = LoginViewModel(strava, userState)
        viewModel.uiModel.observe(lifecycleOwner, observer)
    }

    @Test
    fun `submitAuthCode success emits loading then success`() {
        coroutineRule.testDispatcher.runBlockingTest {
            lifecycleOwner.onResume()
            whenever(strava.authenticate(code)).thenReturn(LoginResult.Success)

            viewModel.submitAuthCode(code)

            verify(strava).authenticate(code)
            inOrder(observer) {
                verify(observer).onChanged(LoginState.Loading)
                verify(observer).onChanged(LoginState.Success)
            }
        }
    }

    @Test
    fun `submitAuthCode error emits loading then idle`() {
        coroutineRule.testDispatcher.runBlockingTest {
            lifecycleOwner.onResume()
            whenever(strava.authenticate(code)).thenReturn(LoginResult.Error(Throwable()))

            viewModel.submitAuthCode(code)

            verify(strava).authenticate(code)
            inOrder(observer) {
                verify(observer).onChanged(LoginState.Loading)
                verify(observer).onChanged(LoginState.Idle)
            }
        }
    }

    @Test
    fun `signOut clears user state`() {
        viewModel.signOut()

        verify(userState).clear()
    }

    @After
    fun tearDown() {
        lifecycleOwner.onDestroy()
    }
}
