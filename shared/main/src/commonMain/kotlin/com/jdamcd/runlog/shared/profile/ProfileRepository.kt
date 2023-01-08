package com.jdamcd.runlog.shared.profile

import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.api.StravaApi
import com.jdamcd.runlog.shared.database.AthleteDao

internal class ProfileRepository(
    private val stravaApi: StravaApi,
    private val dao: AthleteDao,
    private val mapper: ProfileMapper
) {

    suspend fun profile(): AthleteProfile {
        val athlete = stravaApi.athlete()
        val stats = stravaApi.athleteStats(athlete.id)

        // TODO: Separate read & write paths
        dao.insert(
            mapper.athleteToDb(athlete, isUser = true),
            mapper.runStatsToDb(athlete.id, stats)
        )
        return mapper.dbToUi(dao.getUser())
    }
}
