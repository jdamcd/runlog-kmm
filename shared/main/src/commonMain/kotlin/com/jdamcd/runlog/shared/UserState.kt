package com.jdamcd.runlog.shared

import com.jdamcd.runlog.shared.api.TokenProvider

interface UserState {
    var accessToken: String
    var refreshToken: String
    fun isLoggedIn(): Boolean
    fun clear()
}

expect class PersistingUserState : UserState, TokenProvider {
    override var accessToken: String
    override var refreshToken: String
    override fun isLoggedIn(): Boolean
    override fun clear()
}
