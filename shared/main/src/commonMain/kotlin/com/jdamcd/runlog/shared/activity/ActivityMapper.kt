package com.jdamcd.runlog.shared.activity

import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.ActivityDetails
import com.jdamcd.runlog.shared.ActivitySubtype
import com.jdamcd.runlog.shared.ActivityType
import com.jdamcd.runlog.shared.ImageUrl
import com.jdamcd.runlog.shared.Split
import com.jdamcd.runlog.shared.api.ApiDetailedActivity
import com.jdamcd.runlog.shared.api.ApiPolylineMap
import com.jdamcd.runlog.shared.api.ApiSplit
import com.jdamcd.runlog.shared.api.ApiSummaryActivity
import com.jdamcd.runlog.shared.api.MapboxStatic
import com.jdamcd.runlog.shared.util.Formatter.formatDuration
import com.jdamcd.runlog.shared.util.Formatter.formatElevation
import com.jdamcd.runlog.shared.util.Formatter.formatKm
import com.jdamcd.runlog.shared.util.Formatter.formatPace
import com.jdamcd.runlog.shared.util.calculatePace
import com.jdamcd.runlog.shared.util.formatDate
import comjdamcdrunlogshareddatabase.Activity
import kotlinx.datetime.Clock
import kotlin.math.roundToInt

internal class ActivityMapper(private val clock: Clock) {

    private val datePattern = "EEEE dd MMM @ h:mma"
    private val privateEmoji = "\uD83D\uDD12"

    fun summaryApiToDb(activity: ApiSummaryActivity): Activity {
        val subtype = ApiWorkoutType.map(activity.workout_type).toActivitySubtype()
        val paceTime = if (subtype.isRace()) activity.elapsed_time else activity.moving_time
        return Activity(
            id = activity.id,
            name = activity.name,
            isPrivate = activity.private,
            type = mapType(activity.type).name,
            subtype = subtype.name,
            distance = activity.distance,
            duration = paceTime,
            pace = calculatePace(activity.distance, paceTime),
            start = activity.start_date_local,
            mapPolyline = mapPolyline(activity.map),
            lastUpdated = clock.now().epochSeconds
        )
    }

    fun summaryDbToUi(activity: Activity) = ActivityCard(
        id = activity.id,
        name = mapName(activity.name, activity.isPrivate),
        type = ActivityType.valueOf(activity.type),
        subtype = ActivitySubtype.valueOf(activity.subtype),
        distance = formatKm(activity.distance),
        duration = formatDuration(activity.duration),
        pace = formatPace(activity.pace),
        start = formatStartTime(activity.start),
        mapUrl = activity.mapPolyline?.let { pathToImageUrl(it) }
    )

    fun detailApiToUi(activity: ApiDetailedActivity): ActivityDetails {
        val subtype = ApiWorkoutType.map(activity.workout_type).toActivitySubtype()
        return ActivityDetails(
            id = activity.id,
            name = mapName(activity.name, activity.private),
            description = activity.description?.ifEmpty { null },
            type = mapType(activity.type),
            subtype = subtype,
            kudos = activity.kudos_count,
            distance = formatKm(activity.distance),
            elapsedDuration = formatDuration(activity.elapsed_time),
            movingDuration = formatDuration(activity.moving_time),
            elevationGain = formatElevation(activity.total_elevation_gain),
            elevationLow = activity.elev_low?.let { formatElevation(it) },
            elevationHigh = activity.elev_high?.let { formatElevation(it) },
            effort = activity.suffer_score?.roundToInt(),
            calories = activity.calories.roundToInt(),
            averageHeartrate = activity.average_heartrate?.roundToInt(),
            maxHeartrate = activity.max_heartrate?.roundToInt(),
            pace = mapPace(activity.elapsed_time, activity.moving_time, activity.distance, subtype.isRace()),
            start = formatStartTime(activity.start_date_local),
            mapUrl = mapPolyline(activity.map)?.let { pathToImageUrl(it) },
            splits = mapSplits(activity.splits_metric)
        )
    }

    private fun mapName(name: String, private: Boolean) =
        if (private) "$privateEmoji $name" else name

    private fun mapPace(
        elapsedTime: Int,
        movingTime: Int,
        distanceMetres: Float,
        isRace: Boolean
    ): String {
        val time = if (isRace) elapsedTime else movingTime
        return formatPace(calculatePace(distanceMetres, time))
    }

    private fun formatStartTime(startDateLocal: String): String {
        return startDateLocal.formatDate(datePattern).uppercase()
    }

    private fun mapPolyline(map: ApiPolylineMap?): String? {
        return map?.takeIf { it.summary_polyline.isNotEmpty() }?.summary_polyline
    }

    private fun pathToImageUrl(path: String) = ImageUrl(
        default = MapboxStatic.makeUrl(path),
        darkMode = MapboxStatic.makeUrl(path, darkMode = true)
    )

    private fun mapSplits(splits: List<ApiSplit>?): List<Split> {
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
                distance = formatKm(it.distance, withUnit = false),
                isPartial = it.distance < 950, // Not always returned as exactly 1000m
                elapsedDuration = formatDuration(it.elapsed_time),
                movingDuration = formatDuration(it.moving_time),
                elevation = it.elevation_difference?.roundToInt()?.toString() ?: "-",
                averageHeartrate = it.average_heartrate?.roundToInt()?.toString() ?: "-",
                pace = formatPace(paceSeconds, withUnit = false),
                paceSeconds = paceSeconds,
                paceZone = it.pace_zone,
                visualisation = visualiseRelativePace(paceSeconds, min, max)
            )
        }.orEmpty()

        return if (mappedSplits.size >= 2) {
            mappedSplits
        } else {
            emptyList()
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
