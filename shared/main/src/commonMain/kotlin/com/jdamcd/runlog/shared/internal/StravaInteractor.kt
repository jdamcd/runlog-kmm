package com.jdamcd.runlog.shared.internal

import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.LoginResult
import com.jdamcd.runlog.shared.Result
import com.jdamcd.runlog.shared.Strava
import com.jdamcd.runlog.shared.api.AuthException
import com.jdamcd.runlog.shared.api.StravaApi

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
            Result.Error(error, recoverable = error !is AuthException)
        }
    }

    override suspend fun profile(): Result<AthleteProfile> {
        return try {
            val athlete = stravaApi.athlete()
            val athleteStats = stravaApi.athleteStats(athlete.id)
            Result.Data(Mapper.mapProfile(athlete, athleteStats))
        } catch (error: Throwable) {
            Result.Error(error, recoverable = error !is AuthException)
        }
    }

    override val loginUrl = StravaApi.loginUrl()

    override val authScheme = StravaApi.AUTH_SCHEME

    override fun linkUrl(id: Long) = StravaApi.linkUrl(id)
}
