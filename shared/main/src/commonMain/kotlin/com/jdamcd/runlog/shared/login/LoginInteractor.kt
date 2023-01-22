package com.jdamcd.runlog.shared.login

import com.jdamcd.runlog.shared.LoginResult
import com.jdamcd.runlog.shared.StravaLogin
import com.jdamcd.runlog.shared.api.StravaApi
import com.jdamcd.runlog.shared.api.StravaUrl

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

    override val loginUrl = StravaUrl.loginUrl()

    override val authScheme = StravaUrl.AUTH_SCHEME
}
