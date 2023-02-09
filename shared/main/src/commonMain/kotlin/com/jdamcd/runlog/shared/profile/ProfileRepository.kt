package com.jdamcd.runlog.shared.profile

import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.StravaProfile
import com.jdamcd.runlog.shared.api.StravaApi
import com.jdamcd.runlog.shared.database.AthleteDao
import com.jdamcd.runlog.shared.util.MultiLog
import com.jdamcd.runlog.shared.util.RefreshState
import com.jdamcd.runlog.shared.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class ProfileRepository(
    private val api: StravaApi,
    private val dao: AthleteDao,
    private val mapper: AthleteMapper
) : StravaProfile {

    override suspend fun refresh(): RefreshState {
        return try {
            val athlete = api.athlete()
            val stats = api.athleteStats(athlete.id)

            dao.insert(
                mapper.athleteToDb(athlete, isUser = true),
                mapper.runStatsToDb(athlete.id, stats)
            )
            RefreshState.SUCCESS
        } catch (e: Exception) {
            MultiLog.error("Failed to update profile: ${e.message}")
            RefreshState.ERROR
        }
    }

    override suspend fun profile(): Result<AthleteProfile> {
        return dao.user()?.let {
            Result.Data(mapper.dbToUi(it))
        } ?: Result.Empty
    }

    override fun profileFlow(): Flow<Result<AthleteProfile>> {
        return dao.userFlow().map {
            it?.let {
                Result.Data(mapper.dbToUi(it))
            } ?: Result.Empty
        }
    }

    override suspend fun userImageUrl(): String? {
        return withContext(Dispatchers.Default) {
            dao.userImageUrl()
        }
    }
}
