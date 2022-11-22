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
    val distance: Float,
    val moving_time: Int,
    val elapsed_time: Int,
    val start_date_local: String,
    val map: ApiPolylineMap?
)

@Serializable
data class ApiDetailedActivity(
    val id: Long,
    val name: String,
    val description: String,
    val type: String,
    val workout_type: Int?,
    val distance: Float,
    val moving_time: Int,
    val elapsed_time: Int,
    val total_elevation_gain: Float,
    val calories: Float,
    val start_date_local: String,
    val map: ApiPolylineMap?
)

@Serializable
data class ApiPolylineMap(
    val summary_polyline: String
)

@Serializable
data class ApiDetailedAthlete(
    val id: Long,
    val username: String,
    val firstname: String,
    val lastname: String,
    val profile: String? // 124px image URL
)

@Serializable
data class ApiActivityStats(
    val recent_run_totals: ApiActivityTotal,
    val ytd_run_totals: ApiActivityTotal,
    val all_run_totals: ApiActivityTotal
)

@Serializable
data class ApiActivityTotal(
    val count: Int,
    val distance: Float,
    val moving_time: Int
)
