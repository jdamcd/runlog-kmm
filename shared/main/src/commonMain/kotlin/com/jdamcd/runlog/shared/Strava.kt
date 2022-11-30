package com.jdamcd.runlog.shared

interface Strava {
    suspend fun authenticate(code: String): LoginResult
    suspend fun activities(): Result<List<ActivityCard>>
    suspend fun activityDetails(id: Long): Result<ActivityDetails>
    suspend fun profile(): Result<AthleteProfile>
    val loginUrl: String
    val authScheme: String
    fun linkUrl(id: Long): String
    fun requestDarkModeImages(enabled: Boolean)
}

sealed class LoginResult {
    object Success : LoginResult()
    data class Error(val error: Throwable) : LoginResult()
}

sealed class Result<T> {
    data class Data<T>(val data: T) : Result<T>()
    data class Error<Nothing>(val error: Throwable, val recoverable: Boolean) : Result<Nothing>()
}

data class ActivityCard(
    val id: Long,
    val name: String,
    val type: ActivityType,
    val subtype: ActivitySubtype,
    val distance: String,
    val duration: String,
    val pace: String,
    val start: String,
    val mapUrl: String?
)

data class ActivityDetails(
    val id: Long,
    val name: String,
    val description: String?,
    val type: ActivityType,
    val subtype: ActivitySubtype,
    val kudos: Int,
    val distance: String,
    val elapsedDuration: String,
    val movingDuration: String,
    val elevationGain: String,
    val elevationLow: String?,
    val elevationHigh: String?,
    val effort: Int?,
    val calories: Int,
    val averageHeartrate: Int?,
    val maxHeartrate: Int?,
    val pace: String,
    val start: String,
    val mapUrl: String?,
    val splitsInfo: KmSplits?
)

data class KmSplits(
    val splits: List<Split>,
    val minSeconds: Int,
    val maxSeconds: Int,
    val hasHeartrate: Boolean
)

data class Split(
    val number: Int,
    val distance: String,
    val isPartial: Boolean,
    val elapsedDuration: String,
    val movingDuration: String,
    val elevation: Int,
    val averageHeartrate: Int?,
    val pace: String,
    val paceSeconds: Int,
    val paceZone: Int
)

enum class ActivityType {
    RUN, CYCLE, CROSS_TRAIN
}

enum class ActivitySubtype {
    DEFAULT, RACE, WORKOUT, LONG;

    fun isRace() = this == RACE
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
