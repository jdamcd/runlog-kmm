package com.jdamcd.runlog.shared.internal

import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.Result
import com.jdamcd.runlog.shared.StravaProfile
import com.jdamcd.runlog.shared.api.StravaApi

internal class ProfileInteractor(
    private val stravaApi: StravaApi,
    private val mapper: ProfileMapper
) : StravaProfile {

    override suspend fun profile(): Result<AthleteProfile> = tryCall {
        val athlete = stravaApi.athlete()
        val athleteStats = stravaApi.athleteStats(athlete.id)
        Result.Data(mapper.mapProfile(athlete, athleteStats))
    }
}
