package com.jdamcd.runlog.android.login

import app.cash.turbine.test
import com.jdamcd.runlog.android.util.TestCoroutinesRule
import com.jdamcd.runlog.shared.LoginResult
import com.jdamcd.runlog.shared.Strava
import com.jdamcd.runlog.shared.UserState
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
class LoginViewModelTest {

    @get:Rule val coroutineRule = TestCoroutinesRule()

    private val strava: Strava = mock()
    private val userState: UserState = mock()

    private lateinit var viewModel: LoginViewModel

    private val code = "123"

    @Before
    fun setUp() {
        viewModel = LoginViewModel(strava, userState)
    }

    @Test
    fun `startLogin emits loading`() = runTest {
        viewModel.flow.test {
            awaitItem() shouldBe LoginState.Idle

            viewModel.startLogin()

            awaitItem() shouldBe LoginState.Loading
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `submitAuthCode success emits loading then success`() = runTest {
        whenever(strava.authenticate(code)).thenReturn(LoginResult.Success)

        viewModel.flow.test {
            awaitItem() shouldBe LoginState.Idle

            viewModel.submitAuthCode(code)

            awaitItem() shouldBe LoginState.Loading
            awaitItem() shouldBe LoginState.Success
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `submitAuthCode error emits loading then idle`() = runTest {
        whenever(strava.authenticate(code)).thenReturn(LoginResult.Error(Throwable()))

        viewModel.flow.test {
            awaitItem() shouldBe LoginState.Idle

            viewModel.submitAuthCode(code)

            awaitItem() shouldBe LoginState.Loading
            awaitItem() shouldBe LoginState.Idle
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `signOut clears user state`() {
        viewModel.signOut()

        verify(userState).clear()
    }
}
