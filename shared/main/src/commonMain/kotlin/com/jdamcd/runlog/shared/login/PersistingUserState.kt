package com.jdamcd.runlog.shared.login

import com.jdamcd.runlog.shared.api.TokenProvider

internal expect class PersistingUserState : TokenProvider {
    override var accessToken: String
    override var refreshToken: String
    override fun isLoggedIn(): Boolean
    fun clear()
}
