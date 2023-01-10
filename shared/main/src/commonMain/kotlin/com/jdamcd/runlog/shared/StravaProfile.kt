package com.jdamcd.runlog.shared

import kotlinx.coroutines.flow.Flow

interface StravaProfile {
    suspend fun profile(): Result<AthleteProfile>
    fun profileFlow(): Flow<AthleteProfile>
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
