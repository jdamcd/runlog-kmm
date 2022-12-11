package com.jdamcd.runlog.shared.internal

import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.AthleteStats
import com.jdamcd.runlog.shared.api.ApiActivityStats
import com.jdamcd.runlog.shared.api.ApiActivityTotal
import com.jdamcd.runlog.shared.api.ApiDetailedAthlete
import com.jdamcd.runlog.shared.internal.Utils.calculatePace

internal class ProfileMapper {

    fun mapProfile(athlete: ApiDetailedAthlete, athleteStats: ApiActivityStats): AthleteProfile {
        return AthleteProfile(
            id = athlete.id,
            username = athlete.username,
            name = "${athlete.firstname} ${athlete.lastname}".trim(),
            imageUrl = athlete.profile,
            recentRuns = mapStats(athleteStats.recent_run_totals),
            yearRuns = mapStats(athleteStats.ytd_run_totals),
            allRuns = mapStats(athleteStats.all_run_totals)
        )
    }

    private fun mapStats(stats: ApiActivityTotal): AthleteStats {
        return AthleteStats(
            distance = stats.distance.formatKm(),
            pace = calculatePace(stats.distance, stats.moving_time).formatPace()
        )
    }
}
