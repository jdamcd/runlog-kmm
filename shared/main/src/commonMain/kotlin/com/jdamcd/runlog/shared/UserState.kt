package com.jdamcd.runlog.shared

expect class UserState {
    var accessToken: String
    var refreshToken: String
    fun isLoggedIn(): Boolean
    fun clear()
}
