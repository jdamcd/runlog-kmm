package com.jdamcd.runlog.shared.testutil

import com.jdamcd.runlog.shared.api.ApiActivityStats
import com.jdamcd.runlog.shared.api.ApiActivityTotal
import com.jdamcd.runlog.shared.api.ApiDetailedActivity
import com.jdamcd.runlog.shared.api.ApiDetailedAthlete
import com.jdamcd.runlog.shared.api.ApiPolylineMap
import com.jdamcd.runlog.shared.api.ApiSplit
import com.jdamcd.runlog.shared.api.ApiSummaryActivity
import kotlin.math.roundToInt

fun activityModel(
    workout_type: Int? = null,
    map: ApiPolylineMap? = map()
) = ApiSummaryActivity(
    id = 123L,
    name = "my activity",
    private = false,
    type = "Run",
    workout_type = workout_type,
    distance = 10_100.0f,
    moving_time = 2400,
    elapsed_time = 2460,
    start_date_local = "2022-10-25T17:58:50Z",
    map = map
)

fun map() = ApiPolylineMap(
    summary_polyline = "abc"
)

fun detailedActivityModel() = ApiDetailedActivity(
    id = 123L,
    name = "my activity",
    private = false,
    description = "my description",
    type = "Run",
    workout_type = 2,
    kudos_count = 0,
    distance = 10_100.0f,
    moving_time = 2400,
    elapsed_time = 2460,
    total_elevation_gain = 10.0f,
    elev_low = 5.0f,
    elev_high = 15.0f,
    suffer_score = 50.0f,
    calories = 200.0f,
    average_heartrate = 160.0f,
    max_heartrate = 180.0f,
    start_date_local = "2022-10-25T17:58:50Z",
    map = ApiPolylineMap(
        summary_polyline = ""
    ),
    splits_metric = listOf(
        ApiSplit(
            split = 1,
            distance = 1000.0f,
            elapsed_time = 360,
            moving_time = 310,
            elevation_difference = 10.0f,
            average_heartrate = 160.0f,
            pace_zone = 2
        ),
        ApiSplit(
            split = 2,
            distance = 500.0f,
            elapsed_time = 300,
            moving_time = 150,
            elevation_difference = 20.0f,
            average_heartrate = 180.0f,
            pace_zone = 2
        )
    )
)

fun athleteModel() = ApiDetailedAthlete(
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
    moving_time = (distance / 3).toDouble()
)
