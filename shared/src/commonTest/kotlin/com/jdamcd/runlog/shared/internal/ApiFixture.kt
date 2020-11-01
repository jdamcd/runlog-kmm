package com.jdamcd.runlog.shared.internal

fun apiModel(workout_type: Int? = null) = ApiSummaryActivity(
    id = 123L,
    name = "my activity",
    type = "Run",
    workout_type = workout_type,
    distance = 10100.0,
    moving_time = 2400L,
    elapsed_time = 2460L
)
