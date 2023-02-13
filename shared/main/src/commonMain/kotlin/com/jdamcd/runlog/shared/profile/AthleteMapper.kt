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
import kotlinx.datetime.Clock

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
        recentDistance = activityStats.recent_run_totals.distance,
        recentPace = pace(activityStats.recent_run_totals),
        yearDistance = activityStats.ytd_run_totals.distance,
        yearPace = pace(activityStats.ytd_run_totals),
        allDistance = activityStats.all_run_totals.distance,
        allPace = pace(activityStats.all_run_totals),
        lastUpdated = clock.now().epochSeconds
    )

    private fun pace(totals: ApiActivityTotal) =
        calculatePace(totals.distance, totals.moving_time)

    fun dbToUi(athlete: AthleteWithStats) = AthleteProfile(
        id = athlete.id,
        username = athlete.username,
        name = athlete.name,
        imageUrl = athlete.imageUrl,
        recentRuns = mapStats(athlete.recentDistance, athlete.recentPace),
        yearRuns = mapStats(athlete.yearDistance, athlete.yearPace),
        allRuns = mapStats(athlete.allDistance, athlete.allPace)
    )

    private fun mapStats(distance: Float, pace: Int) = AthleteStats(
        distance = formatKm(distance),
        pace = formatPace(pace)
    )
}
