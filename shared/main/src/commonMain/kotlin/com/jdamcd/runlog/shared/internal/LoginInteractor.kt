package com.jdamcd.runlog.shared.internal

import com.jdamcd.runlog.shared.LoginResult
import com.jdamcd.runlog.shared.StravaLogin
import com.jdamcd.runlog.shared.api.StravaApi

internal class LoginInteractor(
    private val stravaApi: StravaApi
) : StravaLogin {

    override suspend fun authenticate(code: String): LoginResult {
        return try {
            stravaApi.tokenExchange(code)
            LoginResult.Success
        } catch (error: Throwable) {
            LoginResult.Error(error)
        }
    }

    override val loginUrl = StravaApi.loginUrl()

    override val authScheme = StravaApi.AUTH_SCHEME
}
