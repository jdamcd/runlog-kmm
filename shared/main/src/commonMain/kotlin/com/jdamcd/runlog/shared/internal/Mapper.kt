package com.jdamcd.runlog.shared.internal

import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.api.ApiSummaryActivity
import com.jdamcd.runlog.shared.formatDate
import com.jdamcd.runlog.shared.formatDuration
import com.jdamcd.runlog.shared.formatKm

internal object Mapper {

    private const val DATE_PATTERN = "EEEE dd MMM @ h:mma"

    fun mapActivityRow(input: ApiSummaryActivity): ActivityCard {
        val type = WorkoutType.map(input.workout_type ?: 0)
        return ActivityCard(
            id = input.id,
            name = input.name,
            type = input.type,
            label = type.label,
            distance = input.distance.formatKm(),
            duration = pickDuration(input, type).formatDuration(),
            start = input.start_date_local.formatDate(DATE_PATTERN)
        )
    }

    private fun pickDuration(input: ApiSummaryActivity, type: WorkoutType) =
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
