package com.jdamcd.runlog.shared.internal

import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.formatDuration
import com.jdamcd.runlog.shared.formatKm
import kotlinx.serialization.Serializable

internal object Mapper {

    fun mapActivityRow(input: ApiSummaryActivity): ActivityCard {
        val type = WorkoutType.map(input.workout_type ?: 0)
        return ActivityCard(
            id = input.id,
            name = input.name,
            type = input.type,
            label = type.label,
            distance = input.distance.formatKm(),
            time = mapTime(input, type).formatDuration()
        )
    }

    private fun mapTime(input: ApiSummaryActivity, type: WorkoutType) =
        if (type.isRace()) input.elapsed_time else input.moving_time

    private enum class WorkoutType(val id: Int, val label: String?) {
        RUN_DEFAULT(0, null),
        RUN_RACE(1, "Race"),
        RUN_LONG(2, "Long"),
        RUN_WORKOUT(3, "Workout"),
        RIDE_DEFAULT(10, null),
        RIDE_RACE(11, "Race"),
        RIDE_WORKOUT(12, "Workout");

        fun isRace() = this == RUN_RACE || this == RIDE_RACE

        companion object {
            fun map(id: Int): WorkoutType = values().first { it.id == id }
        }
    }
}

@Serializable
data class ApiToken(
    val access_token: String,
    val refresh_token: String,
)

@Serializable
data class ApiSummaryActivity(
    val id: Long,
    val name: String,
    val type: String,
    val workout_type: Int?,
    val distance: Double,
    val moving_time: Long,
    val elapsed_time: Long
)
