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
    val elapsed_time: Long
)
