package com.jdamcd.runlog.shared.internal

import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.ActivityDetails
import com.jdamcd.runlog.shared.ActivitySubtype
import com.jdamcd.runlog.shared.ActivityType
import com.jdamcd.runlog.shared.KmSplits
import com.jdamcd.runlog.shared.Split
import com.jdamcd.runlog.shared.api.ApiDetailedActivity
import com.jdamcd.runlog.shared.api.ApiPolylineMap
import com.jdamcd.runlog.shared.api.ApiSplit
import com.jdamcd.runlog.shared.api.ApiSummaryActivity
import com.jdamcd.runlog.shared.api.MapboxStatic
import com.jdamcd.runlog.shared.formatDate
import com.jdamcd.runlog.shared.formatDuration
import com.jdamcd.runlog.shared.formatElevation
import com.jdamcd.runlog.shared.formatKm
import com.jdamcd.runlog.shared.formatPace
import com.jdamcd.runlog.shared.internal.Utils.calculatePace
import kotlin.math.roundToInt

internal class ActivityMapper {

    private val datePattern = "EEEE dd MMM @ h:mma"
    private val privateEmoji = "\uD83D\uDD12"

    var darkModeImages = false

    fun mapActivityCard(activity: ApiSummaryActivity): ActivityCard {
        val subtype = ApiWorkoutType.map(activity.workout_type).toActivitySubtype()
        return ActivityCard(
            id = activity.id,
            name = mapName(activity.name, activity.private),
            type = mapType(activity.type),
            subtype = subtype,
            distance = activity.distance.formatKm(),
            duration = mapDuration(activity.elapsed_time, activity.moving_time, subtype.isRace()),
            pace = mapPace(activity.elapsed_time, activity.moving_time, activity.distance, subtype.isRace()),
            start = mapStartTime(activity.start_date_local),
            mapUrl = mapMap(activity.map)
        )
    }

    fun mapActivityDetails(activity: ApiDetailedActivity): ActivityDetails {
        val subtype = ApiWorkoutType.map(activity.workout_type).toActivitySubtype()
        return ActivityDetails(
            id = activity.id,
            name = mapName(activity.name, activity.private),
            description = activity.description?.ifEmpty { null },
            type = mapType(activity.type),
            subtype = subtype,
            kudos = activity.kudos_count,
            distance = activity.distance.formatKm(),
            elapsedDuration = activity.elapsed_time.formatDuration(),
            movingDuration = activity.moving_time.formatDuration(),
            elevationGain = activity.total_elevation_gain.formatElevation(),
            elevationLow = activity.elev_low?.formatElevation(),
            elevationHigh = activity.elev_high?.formatElevation(),
            effort = activity.suffer_score?.roundToInt(),
            calories = activity.calories.roundToInt(),
            averageHeartrate = activity.average_heartrate?.roundToInt(),
            maxHeartrate = activity.max_heartrate?.roundToInt(),
            pace = mapPace(activity.elapsed_time, activity.moving_time, activity.distance, subtype.isRace()),
            start = mapStartTime(activity.start_date_local),
            mapUrl = mapMap(activity.map),
            splitsInfo = mapSplits(activity.splits_metric)
        )
    }

    private fun mapName(name: String, private: Boolean) =
        if (private) "$privateEmoji $name" else name

    private fun mapDuration(elapsedTime: Int, movingTime: Int, isRace: Boolean): String {
        val time = if (isRace) elapsedTime else movingTime
        return time.formatDuration()
    }

    private fun mapPace(
        elapsedTime: Int,
        movingTime: Int,
        distanceMetres: Float,
        isRace: Boolean
    ): String {
        val time = if (isRace) elapsedTime else movingTime
        return calculatePace(distanceMetres, time).formatPace()
    }

    private fun mapStartTime(startDateLocal: String): String {
        return startDateLocal.formatDate(datePattern).uppercase()
    }

    private fun mapMap(map: ApiPolylineMap?): String? {
        return map?.takeIf { it.summary_polyline.isNotEmpty() }
            ?.let { MapboxStatic.makeUrl(it.summary_polyline, darkMode = darkModeImages) }
    }

    private fun mapSplits(splits: List<ApiSplit>?): KmSplits? {
        val paces = splits?.associateBy(
            { it.split },
            { calculatePace(it.distance, it.moving_time) }
        ).orEmpty()
        val min = paces.minOfOrNull { it.value } ?: 0
        val max = paces.minOfOrNull { it.value } ?: 0

        val mappedSplits = splits?.filter { it.distance >= 200 }?.map {
            val paceSeconds = paces[it.split]!!
            Split(
                number = it.split,
                distance = it.distance.formatKm(withUnit = false),
                isPartial = it.distance < 950, // Not always returned as exactly 1000m
                elapsedDuration = it.elapsed_time.formatDuration(),
                movingDuration = it.moving_time.formatDuration(),
                elevation = it.elevation_difference.roundToInt(),
                averageHeartrate = it.average_heartrate?.roundToInt(),
                pace = paceSeconds.formatPace(withUnit = false),
                paceSeconds = paceSeconds,
                paceZone = it.pace_zone,
                visualisation = visualiseRelativePace(paceSeconds, min, max)
            )
        }.orEmpty()

        return if (mappedSplits.size >= 2) {
            KmSplits(
                splits = mappedSplits,
                hasHeartrate = mappedSplits[0].averageHeartrate != null
            )
        } else {
            null
        }
    }

    private fun visualiseRelativePace(paceSeconds: Int, min: Int, max: Int): Float {
        val value = ((max - paceSeconds) / max.toFloat()) + (min / max.toFloat())
        return value.coerceIn(0.0f, 1.0f)
    }

    private fun mapType(type: String): ActivityType {
        return when (type) {
            "Run" -> ActivityType.RUN
            "Ride" -> ActivityType.CYCLE
            else -> ActivityType.CROSS_TRAIN
        }
    }

    private enum class ApiWorkoutType(val id: Int) {
        RUN_DEFAULT(0),
        RUN_RACE(1),
        RUN_LONG(2),
        RUN_WORKOUT(3),
        RIDE_DEFAULT(10),
        RIDE_RACE(11),
        RIDE_WORKOUT(12);

        fun toActivitySubtype(): ActivitySubtype {
            return when (this.id) {
                RUN_RACE.id, RIDE_RACE.id -> ActivitySubtype.RACE
                RUN_WORKOUT.id, RIDE_WORKOUT.id -> ActivitySubtype.WORKOUT
                RUN_LONG.id -> ActivitySubtype.LONG
                else -> ActivitySubtype.DEFAULT
            }
        }

        companion object {
            fun map(id: Int?): ApiWorkoutType = values().firstOrNull { it.id == id } ?: RUN_DEFAULT
        }
    }
}
