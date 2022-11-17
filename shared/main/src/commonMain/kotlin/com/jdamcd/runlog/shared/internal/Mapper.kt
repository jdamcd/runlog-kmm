package com.jdamcd.runlog.shared.internal

import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.ActivityType
import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.api.ApiAthlete
import com.jdamcd.runlog.shared.api.ApiAthleteStats
import com.jdamcd.runlog.shared.api.ApiSummaryActivity
import com.jdamcd.runlog.shared.api.MapboxStatic
import com.jdamcd.runlog.shared.formatDate
import com.jdamcd.runlog.shared.formatDuration
import com.jdamcd.runlog.shared.formatKm
import com.jdamcd.runlog.shared.formatPace
import kotlin.math.roundToInt

internal object Mapper {

    private const val DATE_PATTERN = "EEEE dd MMM @ h:mma"

    fun mapActivityRow(activity: ApiSummaryActivity): ActivityCard {
        val type = ApiWorkoutType.map(activity.workout_type ?: 0)
        return ActivityCard(
            id = activity.id,
            name = activity.name,
            type = type.toActivityType(),
            isRace = type.isRace(),
            distance = activity.distance.formatKm(),
            duration = mapDuration(activity, type),
            pace = mapPace(activity, type),
            start = mapStartTime(activity),
            mapUrl = activity.map?.let { MapboxStatic.makeUrl(it.summary_polyline) }
        )
    }

    private fun mapDuration(activity: ApiSummaryActivity, type: ApiWorkoutType): String {
        val time = if (type.isRace()) activity.elapsed_time else activity.moving_time
        return time.formatDuration()
    }

    private fun mapPace(activity: ApiSummaryActivity, type: ApiWorkoutType): String {
        val time = if (type.isRace()) activity.elapsed_time else activity.moving_time
        val distanceKm = activity.distance / 1000
        val pace = (time / distanceKm).roundToInt() // seconds per km
        return pace.formatPace()
    }

    private fun mapStartTime(activity: ApiSummaryActivity): String {
        return activity.start_date_local.formatDate(DATE_PATTERN).uppercase()
    }

    private enum class ApiWorkoutType(val id: Int) {
        RUN_DEFAULT(0),
        RUN_RACE(1),
        RUN_LONG(2),
        RUN_WORKOUT(3),
        RIDE_DEFAULT(10),
        RIDE_RACE(11),
        RIDE_WORKOUT(12);

        fun isRace() = this == RUN_RACE || this == RIDE_RACE

        fun toActivityType(): ActivityType {
            return ActivityType.RUN
        }

        companion object {
            fun map(id: Int): ApiWorkoutType = values().first { it.id == id }
        }
    }

    fun mapProfile(athlete: ApiAthlete, athleteStats: ApiAthleteStats): AthleteProfile {
        return AthleteProfile(
            id = athlete.id,
            username = athlete.username,
            name = "${athlete.firstname} ${athlete.lastname}".trim(),
            imageUrl = athlete.profile,
            yearRunDistance = athleteStats.ytd_run_totals.distance.formatKm(),
            allTimeRunDistance = athleteStats.all_run_totals.distance.formatKm()
        )
    }
}
