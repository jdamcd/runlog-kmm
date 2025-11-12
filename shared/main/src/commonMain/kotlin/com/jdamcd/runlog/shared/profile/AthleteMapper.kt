package com.jdamcd.runlog.shared.profile

import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.AthleteStats
import com.jdamcd.runlog.shared.api.ApiActivityStats
import com.jdamcd.runlog.shared.api.ApiActivityTotal
import com.jdamcd.runlog.shared.api.ApiDetailedAthlete
import com.jdamcd.runlog.shared.util.Formatter.formatKm
import com.jdamcd.runlog.shared.util.Formatter.formatPace
import com.jdamcd.runlog.shared.util.calculatePace
import comjdamcdrunlogshareddatabase.Athlete
import comjdamcdrunlogshareddatabase.AthleteWithStats
import comjdamcdrunlogshareddatabase.RunStats
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
internal class AthleteMapper(private val clock: Clock) {

    fun athleteToDb(
        athlete: ApiDetailedAthlete,
        isUser: Boolean
    ) = Athlete(
        id = athlete.id,
        username = athlete.username,
        name = "${athlete.firstname} ${athlete.lastname}".trim(),
        imageUrl = athlete.profile,
        isUser = isUser,
        lastUpdated = clock.now().epochSeconds
    )

    fun runStatsToDb(
        id: Long,
        activityStats: ApiActivityStats
    ) = RunStats(
        id = id,
        recentDistance = activityStats.recent_run_totals.distance.toDouble(),
        recentPace = pace(activityStats.recent_run_totals).toLong(),
        yearDistance = activityStats.ytd_run_totals.distance.toDouble(),
        yearPace = pace(activityStats.ytd_run_totals).toLong(),
        allDistance = activityStats.all_run_totals.distance.toDouble(),
        allPace = pace(activityStats.all_run_totals).toLong(),
        lastUpdated = clock.now().epochSeconds
    )

    private fun pace(totals: ApiActivityTotal) = calculatePace(totals.distance, totals.moving_time)

    fun dbToUi(athlete: AthleteWithStats) = AthleteProfile(
        id = athlete.id,
        username = athlete.username,
        name = athlete.name,
        imageUrl = athlete.imageUrl,
        recentRuns = mapStats(athlete.recentDistance.toFloat(), athlete.recentPace.toInt()),
        yearRuns = mapStats(athlete.yearDistance.toFloat(), athlete.yearPace.toInt()),
        allRuns = mapStats(athlete.allDistance.toFloat(), athlete.allPace.toInt())
    )

    private fun mapStats(distance: Float, pace: Int) = AthleteStats(
        distance = formatKm(distance),
        pace = formatPace(pace)
    )
}
