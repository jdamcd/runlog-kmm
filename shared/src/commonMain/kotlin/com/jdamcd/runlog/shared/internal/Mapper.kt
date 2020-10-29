package com.jdamcd.runlog.shared.internal

import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.formatDuration
import com.jdamcd.runlog.shared.formatKm
import kotlinx.serialization.Serializable

internal object Mapper {

    fun mapActivityRow(input: ApiSummaryActivity) = ActivityCard(
        id = input.id,
        name = input.name,
        type = input.type,
        distance = input.distance.formatKm(),
        movingTime = input.moving_time.formatDuration()
    )
}

@Serializable
data class ApiToken(
    val access_token: String,
    val refresh_token: String,
)

@Serializable
data class ApiSummaryActivity(
    val id: Long,
    val name: String,
    val type: String,
    val distance: Double,
    val moving_time: Long
)
