package com.jdamcd.runlog.shared.internal

import com.jdamcd.runlog.shared.api.ApiAthlete
import com.jdamcd.runlog.shared.api.ApiAthleteStats
import com.jdamcd.runlog.shared.api.ApiStatsBlock
import com.jdamcd.runlog.shared.api.ApiSummaryActivity
import kotlin.math.roundToLong

fun activityModel(workout_type: Int? = null) = ApiSummaryActivity(
    id = 123L,
    name = "my activity",
    type = "Run",
    workout_type = workout_type,
    distance = 10_100.0,
    moving_time = 2400L,
    elapsed_time = 2460L,
    start_date_local = "2022-10-25T17:58:50Z",
    map = null
)

fun athleteModel() = ApiAthlete(
    id = 123L,
    username = "jdamcd",
    firstname = "Jamie",
    lastname = "McDonald",
    profile = "image.url"
)

fun athleteStatsModel() = ApiAthleteStats(
    recent_run_totals = statsBlock(distance = 100_000.0),
    ytd_run_totals = statsBlock(distance = 1_000_000.0),
    all_run_totals = statsBlock(distance = 5_000_000.0)
)

fun statsBlock(distance: Double = 100.0) = ApiStatsBlock(
    count = 123,
    distance = distance,
    moving_time = (distance / 3).roundToLong()
)
