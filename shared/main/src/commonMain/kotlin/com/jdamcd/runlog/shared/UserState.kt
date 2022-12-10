package com.jdamcd.runlog.shared

interface UserState {
    var accessToken: String
    var refreshToken: String
    fun isLoggedIn(): Boolean
    fun clear()
}

expect class PersistingUserState : UserState {
    override var accessToken: String
    override var refreshToken: String
    override fun isLoggedIn(): Boolean
    override fun clear()
}
