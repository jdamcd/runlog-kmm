package com.jdamcd.runlog.shared

import com.jdamcd.runlog.shared.util.RefreshState
import com.jdamcd.runlog.shared.util.Result
import kotlinx.coroutines.flow.Flow

interface StravaProfile {
    suspend fun profile(): Result<AthleteProfile>
    fun profileFlow(): Flow<Result<AthleteProfile>>
    suspend fun refresh(): RefreshState
    suspend fun userImageUrl(): String?
}

data class AthleteProfile(
    val id: Long,
    val username: String,
    val name: String,
    val imageUrl: String?,
    val recentRuns: AthleteStats,
    val yearRuns: AthleteStats,
    val allRuns: AthleteStats
)

data class AthleteStats(
    val distance: String,
    val pace: String
)
