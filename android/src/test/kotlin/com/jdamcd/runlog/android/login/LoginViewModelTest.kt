package com.jdamcd.runlog.android.login

import app.cash.turbine.test
import com.jdamcd.runlog.android.util.TestCoroutinesRule
import com.jdamcd.runlog.shared.LoginResult
import com.jdamcd.runlog.shared.StravaLogin
import com.jdamcd.runlog.shared.UserManager
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class LoginViewModelTest {

    @get:Rule val coroutineRule = TestCoroutinesRule()

    private val stravaLogin: StravaLogin = mockk()
    private val userState: UserManager = mockk()

    private lateinit var viewModel: LoginViewModel

    private val code = "123"

    @Before
    fun setUp() {
        viewModel = LoginViewModel(stravaLogin, userState)
    }

    @Test
    fun `startLogin emits loading`() = runTest {
        every { stravaLogin.loginUrl } returns "url"

        viewModel.flow.test {
            awaitItem() shouldBe LoginState.Idle

            viewModel.startLogin()

            awaitItem() shouldBe LoginState.Loading
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `submitAuthCode success emits loading then success`() = runTest {
        coEvery { stravaLogin.authenticate(code) } returns LoginResult.Success

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
        coEvery { stravaLogin.authenticate(code) } returns LoginResult.Error(Throwable())

        viewModel.flow.test {
            awaitItem() shouldBe LoginState.Idle

            viewModel.submitAuthCode(code)

            awaitItem() shouldBe LoginState.Loading
            awaitItem() shouldBe LoginState.Idle
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `signOut clears user state`() = runTest {
        every { userState.logOut() } just runs

        viewModel.signOut()

        verify { userState.logOut() }
    }
}
