package com.jdamcd.runlog.shared.internal

import com.jdamcd.runlog.shared.api.ApiActivityStats
import com.jdamcd.runlog.shared.api.ApiActivityTotal
import com.jdamcd.runlog.shared.api.ApiSummaryActivity
import com.jdamcd.runlog.shared.api.ApiSummaryAthlete
import kotlin.math.roundToInt

fun activityModel(workout_type: Int? = null) = ApiSummaryActivity(
    id = 123L,
    name = "my activity",
    type = "Run",
    workout_type = workout_type,
    distance = 10_100.0f,
    moving_time = 2400,
    elapsed_time = 2460,
    start_date_local = "2022-10-25T17:58:50Z",
    map = null
)

fun athleteModel() = ApiSummaryAthlete(
    id = 123L,
    username = "jdamcd",
    firstname = "Jamie",
    lastname = "McDonald",
    profile = "image.url"
)

fun athleteStatsModel() = ApiActivityStats(
    recent_run_totals = statsBlock(distance = 100_000.0f),
    ytd_run_totals = statsBlock(distance = 1_000_000.0f),
    all_run_totals = statsBlock(distance = 5_000_000.0f)
)

fun statsBlock(distance: Float = 100.0f) = ApiActivityTotal(
    count = 123,
    distance = distance,
    moving_time = (distance / 3).roundToInt()
)
