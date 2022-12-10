package com.jdamcd.runlog.shared

import io.kotest.matchers.shouldBe
import kotlin.test.BeforeTest
import kotlin.test.Test

class UserStateTest {

    lateinit var userState: PersistingUserState

    @BeforeTest
    fun setUp() {
        userState = PersistingUserState()
        userState.clear()
    }

    @Test
    fun savesAccessToken() {
        userState.accessToken = "abc123"

        userState.accessToken shouldBe "abc123"
    }

    @Test
    fun savesRefreshToken() {
        userState.refreshToken = "123abc"

        userState.refreshToken shouldBe "123abc"
    }

    @Test
    fun notLoggedInWithEmptyAccessToken() {
        userState.isLoggedIn() shouldBe false
    }

    @Test
    fun loggedInWithAccessToken() {
        userState.accessToken = "abc123"

        userState.isLoggedIn() shouldBe true
    }

    @Test
    fun clearsState() {
        userState.accessToken = "abc123"

        userState.clear()

        userState.isLoggedIn() shouldBe false
        userState.accessToken shouldBe ""
    }
}
