package com.jdamcd.runlog.shared

interface Strava {
    suspend fun authenticate(code: String): LoginResult
    suspend fun activities(): Result<List<ActivityCard>>
    suspend fun profile(): Result<AthleteProfile>
    val loginUrl: String
    val authScheme: String
    fun linkUrl(id: Long): String
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
    val isRace: Boolean,
    val distance: String,
    val duration: String,
    val pace: String,
    val start: String,
    val mapUrl: String?
)

enum class ActivityType {
    RUN, CYCLE, CROSS_TRAIN
}

data class AthleteProfile(
    val id: Long,
    val username: String,
    val name: String,
    val imageUrl: String,
    val yearRunDistance: String,
    val allTimeRunDistance: String
)
