package com.jdamcd.runlog.shared

import com.jdamcd.runlog.shared.api.StravaApi
import com.jdamcd.runlog.shared.api.TokenProvider
import com.jdamcd.runlog.shared.internal.ActivityInteractor
import com.jdamcd.runlog.shared.internal.ActivityMapper
import com.jdamcd.runlog.shared.internal.LoginInteractor
import com.jdamcd.runlog.shared.internal.ProfileInteractor
import com.jdamcd.runlog.shared.internal.ProfileMapper

object SharedModule {
    fun stravaLogin(user: UserState): StravaLogin =
        LoginInteractor(StravaApi(UserWrapper(user)))

    fun stravaActivity(user: UserState): StravaActivity =
        ActivityInteractor(StravaApi(UserWrapper(user)), ActivityMapper())

    fun stravaProfile(user: UserState): StravaProfile =
        ProfileInteractor(StravaApi(UserWrapper(user)), ProfileMapper())
}

internal class UserWrapper(private val user: UserState) : TokenProvider {

    override var accessToken: String
        get() = user.accessToken
        set(value) { user.accessToken = value }

    override var refreshToken: String
        get() = user.refreshToken
        set(value) { user.refreshToken = value }

    override fun isLoggedIn(): Boolean {
        return user.isLoggedIn()
    }
}
