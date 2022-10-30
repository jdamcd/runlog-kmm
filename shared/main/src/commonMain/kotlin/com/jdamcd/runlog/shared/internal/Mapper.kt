package com.jdamcd.runlog.shared.internal

import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.api.ApiAthlete
import com.jdamcd.runlog.shared.api.ApiAthleteStats
import com.jdamcd.runlog.shared.api.ApiSummaryActivity
import com.jdamcd.runlog.shared.formatDate
import com.jdamcd.runlog.shared.formatDuration
import com.jdamcd.runlog.shared.formatKm

internal object Mapper {

    private const val DATE_PATTERN = "EEEE dd MMM @ h:mma"

    fun mapActivityRow(activity: ApiSummaryActivity): ActivityCard {
        val type = WorkoutType.map(activity.workout_type ?: 0)
        return ActivityCard(
            id = activity.id,
            name = activity.name,
            type = activity.type,
            label = type.label,
            distance = activity.distance.formatKm(),
            duration = pickDuration(activity, type).formatDuration(),
            start = activity.start_date_local.formatDate(DATE_PATTERN)
        )
    }

    fun mapProfile(athlete: ApiAthlete, athleteStats: ApiAthleteStats): AthleteProfile {
        return AthleteProfile(
            id = athlete.id,
            username = athlete.username,
            name = "${athlete.firstname} ${athlete.lastname}".trim(),
            imageUrl = athlete.profile,
            fourWeekRunDistance = athleteStats.recent_run_totals.distance.formatKm(),
            yearRunDistance = athleteStats.ytd_run_totals.distance.formatKm(),
            allTimeRunDistance = athleteStats.all_run_totals.distance.formatKm()
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
