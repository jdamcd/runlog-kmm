package com.jdamcd.runlog.shared

import com.jdamcd.runlog.shared.api.StravaApi
import com.jdamcd.runlog.shared.api.TokenProvider
import com.jdamcd.runlog.shared.internal.ActivityInteractor
import com.jdamcd.runlog.shared.internal.ActivityMapper
import com.jdamcd.runlog.shared.internal.LoginInteractor
import com.jdamcd.runlog.shared.internal.ProfileInteractor
import com.jdamcd.runlog.shared.internal.ProfileMapper
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object SharedModule {

    fun stravaLogin(user: UserState): StravaLogin =
        LoginInteractor(createApi(user))

    fun stravaActivity(user: UserState): StravaActivity =
        ActivityInteractor(createApi(user), ActivityMapper())

    fun stravaProfile(user: UserState): StravaProfile =
        ProfileInteractor(createApi(user), ProfileMapper())

    private fun createApi(user: UserState): StravaApi {
        if (user.hashCode() != userHash || api == null) {
            userHash = user.hashCode()
            api = StravaApi(UserWrapper(user))
        }
        return api!!
    }
    private var userHash = 0
    private var api: StravaApi? = null
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
