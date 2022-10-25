package com.jdamcd.runlog.shared.internal

import com.jdamcd.runlog.shared.api.ApiSummaryActivity

fun apiModel(workout_type: Int? = null) = ApiSummaryActivity(
    id = 123L,
    name = "my activity",
    type = "Run",
    workout_type = workout_type,
    distance = 10100.0,
    moving_time = 2400L,
    elapsed_time = 2460L,
    start_date_local = "2022-10-25T17:58:50Z"
)
