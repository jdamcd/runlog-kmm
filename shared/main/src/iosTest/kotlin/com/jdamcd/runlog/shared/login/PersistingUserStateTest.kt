package com.jdamcd.runlog.shared.login

import io.kotest.matchers.shouldBe
import kotlin.test.BeforeTest
import kotlin.test.Test

class PersistingUserStateTest {

    private lateinit var userState: PersistingUserState

    @BeforeTest
    fun setUp() {
        userState = PersistingUserState()
        userState.clear()
    }

    @Test
    fun `saves access token`() {
        userState.accessToken = "abc123"

        userState.accessToken shouldBe "abc123"
    }

    @Test
    fun `saves refresh token`() {
        userState.refreshToken = "123abc"

        userState.refreshToken shouldBe "123abc"
    }

    @Test
    fun `user is not logged in if access token is empty`() {
        userState.isLoggedIn() shouldBe false
    }

    @Test
    fun `user is logged in when there is an access token`() {
        userState.accessToken = "abc123"

        userState.isLoggedIn() shouldBe true
    }

    @Test
    fun `clears user state`() {
        userState.accessToken = "abc123"

        userState.clear()

        userState.isLoggedIn() shouldBe false
        userState.accessToken shouldBe ""
    }
}
