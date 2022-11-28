package com.jdamcd.runlog.shared.internal

import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.ActivityDetails
import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.LoginResult
import com.jdamcd.runlog.shared.Result
import com.jdamcd.runlog.shared.Strava
import com.jdamcd.runlog.shared.api.AuthException
import com.jdamcd.runlog.shared.api.StravaApi

internal class StravaInteractor(
    private val stravaApi: StravaApi,
    private val mapper: Mapper
) : Strava {

    override suspend fun authenticate(code: String): LoginResult {
        return try {
            stravaApi.tokenExchange(code)
            LoginResult.Success
        } catch (error: Throwable) {
            LoginResult.Error(error)
        }
    }

    override suspend fun activities(): Result<List<ActivityCard>> = tryCall {
        val activities = stravaApi.activities().map {
            mapper.mapActivityCard(it)
        }
        Result.Data(activities)
    }

    override suspend fun activityDetails(id: Long): Result<ActivityDetails> = tryCall {
        val activityDetails = stravaApi.activity(id)
        Result.Data(mapper.mapActivityDetails(activityDetails))
    }

    override suspend fun profile(): Result<AthleteProfile> = tryCall {
        val athlete = stravaApi.athlete()
        val athleteStats = stravaApi.athleteStats(athlete.id)
        Result.Data(mapper.mapProfile(athlete, athleteStats))
    }

    private inline fun <T> tryCall(call: () -> Result<T>): Result<T> {
        return try {
            call.invoke()
        } catch (error: Throwable) {
            Result.Error(error, recoverable = error !is AuthException)
        }
    }

    override val loginUrl = StravaApi.loginUrl()

    override val authScheme = StravaApi.AUTH_SCHEME

    override fun linkUrl(id: Long) = StravaApi.linkUrl(id)

    override fun requestDarkModeImages(enabled: Boolean) {
        mapper.darkModeImages = enabled
    }
}
