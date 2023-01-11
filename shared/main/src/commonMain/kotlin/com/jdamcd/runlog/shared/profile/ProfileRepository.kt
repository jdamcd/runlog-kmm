package com.jdamcd.runlog.shared.profile

import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.api.StravaApi
import com.jdamcd.runlog.shared.database.AthleteDao
import com.jdamcd.runlog.shared.util.MultiLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

internal class ProfileRepository(
    private val stravaApi: StravaApi,
    private val dao: AthleteDao,
    private val mapper: ProfileMapper
) {

    suspend fun profile(): AthleteProfile {
        fetchProfile()
        return mapper.dbToUi(dao.user())
    }

    suspend fun fetchProfile(): UpdateResult {
        return try {
            val athlete = stravaApi.athlete()
            val stats = stravaApi.athleteStats(athlete.id)

            dao.insert(
                mapper.athleteToDb(athlete, isUser = true),
                mapper.runStatsToDb(athlete.id, stats)
            )
            UpdateResult.Success
        } catch (e: Exception) {
            MultiLog.error("Failed to update profile: ${e.message}")
            UpdateResult.Failure
        }
    }

    fun loadProfile(): Flow<AthleteProfile> {
        return dao.userFlow().map {
            it?.let {
                mapper.dbToUi(it)
            }
        }.filterNotNull()
    }
}

sealed class UpdateResult {
    object Success : UpdateResult()
    object Failure : UpdateResult()
}
