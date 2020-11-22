package com.jdamcd.runlog.shared

import com.jdamcd.runlog.shared.api.StravaApi
import com.jdamcd.runlog.shared.api.TokenProvider
import com.jdamcd.runlog.shared.internal.StravaInteractor

object SharedModule {
    fun buildStrava(userState: UserState): Strava =
        StravaInteractor(StravaApi(UserStateWrapper(userState)))
}

internal class UserStateWrapper(private val userState: UserState) : TokenProvider {

    override var accessToken: String
        get() = userState.accessToken
        set(value) { userState.accessToken = value }

    override var refreshToken: String
        get() = userState.refreshToken
        set(value) { userState.refreshToken = value }

    override fun isLoggedIn(): Boolean {
        return userState.isLoggedIn()
    }
}
