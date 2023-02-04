package com.jdamcd.runlog.shared

import com.jdamcd.runlog.shared.util.Result

interface StravaActivity {
    suspend fun activities(): Result<List<ActivityCard>>
    suspend fun activityDetails(id: Long): Result<ActivityDetails>
    fun linkUrl(id: Long): String
    fun requestDarkModeImages(enabled: Boolean)
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
    val splits: List<Split>
)

data class Split(
    val number: Int,
    val distance: String,
    val isPartial: Boolean,
    val elapsedDuration: String,
    val movingDuration: String,
    val elevation: String,
    val averageHeartrate: String,
    val pace: String,
    val paceSeconds: Int,
    val paceZone: Int,
    val visualisation: Float // Relative size 0.0 - 1.0
)

enum class ActivityType {
    RUN, CYCLE, CROSS_TRAIN
}

enum class ActivitySubtype {
    DEFAULT, RACE, WORKOUT, LONG;

    fun isRace() = this == RACE
}
