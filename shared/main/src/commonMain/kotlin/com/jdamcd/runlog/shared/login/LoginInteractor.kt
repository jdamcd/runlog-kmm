package com.jdamcd.runlog.shared.login

import com.jdamcd.runlog.shared.LoginResult
import com.jdamcd.runlog.shared.StravaLogin
import com.jdamcd.runlog.shared.api.StravaApi
import com.jdamcd.runlog.shared.api.StravaUrl
import com.jdamcd.runlog.shared.database.AthleteDao
import com.jdamcd.runlog.shared.profile.AthleteMapper

internal class LoginInteractor(
    private val stravaApi: StravaApi,
    private val athleteDao: AthleteDao,
    private val athleteMapper: AthleteMapper
) : StravaLogin {

    override suspend fun authenticate(code: String): LoginResult {
        return try {
            val athlete = stravaApi.tokenExchange(code)
            athleteDao.insert(athleteMapper.athleteToDb(athlete, isUser = true))
            LoginResult.Success
        } catch (error: Throwable) {
            LoginResult.Error(error)
        }
    }

    override val loginUrl = StravaUrl.loginUrl()

    override val authScheme = StravaUrl.AUTH_SCHEME
}
