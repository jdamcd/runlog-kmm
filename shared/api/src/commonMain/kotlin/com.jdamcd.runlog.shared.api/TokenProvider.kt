package com.jdamcd.runlog.shared.api

interface TokenProvider {
    var accessToken: String
    var refreshToken: String

    fun isLoggedIn(): Boolean

    fun store(accessToken: String, refreshToken: String) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }
}
