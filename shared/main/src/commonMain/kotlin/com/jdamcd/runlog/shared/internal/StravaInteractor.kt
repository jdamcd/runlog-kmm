package com.jdamcd.runlog.shared.internal

import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.LoginResult
import com.jdamcd.runlog.shared.Result
import com.jdamcd.runlog.shared.Strava
import com.jdamcd.runlog.shared.util.Logger
import io.ktor.client.features.ClientRequestException
import io.ktor.http.HttpStatusCode

internal class StravaInteractor(
    private val stravaApi: StravaApi
) : Strava {

    override suspend fun authenticate(code: String): LoginResult {
        return try {
            stravaApi.tokenExchange(code)
            LoginResult.Success
        } catch (error: Throwable) {
            LoginResult.Error(error)
        }
    }

    override suspend fun activities(): Result<List<ActivityCard>> {
        return try {
            val activities = stravaApi.activities().map {
                Mapper.mapActivityRow(it)
            }
            Result.Data(activities)
        } catch (error: Throwable) {
            val authError = error is ClientRequestException && error.response.status == HttpStatusCode.Unauthorized
            Logger.debug("${error.message}")
            Result.Error(error, !authError)
        }
    }

    override val loginUrl = StravaApi.loginUrl()

    override val authRedirect = StravaApi.AUTH_REDIRECT

    override fun linkUrl(id: Long) = StravaApi.linkUrl(id)
}
