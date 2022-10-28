package com.jdamcd.runlog.shared.api

import kotlinx.serialization.Serializable

@Serializable
data class ApiToken(
    val access_token: String,
    val refresh_token: String
)

@Serializable
data class ApiSummaryActivity(
    val id: Long,
    val name: String,
    val type: String,
    val workout_type: Int?,
    val distance: Double,
    val moving_time: Long,
    val elapsed_time: Long,
    val start_date_local: String
)

@Serializable
data class ApiAthlete(
    val id: Long,
    val username: String,
    val firstname: String,
    val country: String,
    val lastname: String,
    val profile: String
)

@Serializable
data class ApiAthleteStats(
    val ytd_run_totals: ApiStatsBlock,
    val all_run_totals: ApiStatsBlock,
    val ytd_ride_totals: ApiStatsBlock,
    val all_ride_totals: ApiStatsBlock
)

@Serializable
data class ApiStatsBlock(
    val count: Int,
    val distance: Long,
    val moving_time: Long,
    val elapsed_time: Long,
    val elevation_gain: Int
)
