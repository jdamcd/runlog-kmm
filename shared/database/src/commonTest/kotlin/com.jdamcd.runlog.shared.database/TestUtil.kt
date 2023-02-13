package com.jdamcd.runlog.shared.database

import com.squareup.sqldelight.db.SqlDriver
import comjdamcdrunlogshareddatabase.Activity
import comjdamcdrunlogshareddatabase.Athlete
import comjdamcdrunlogshareddatabase.RunStats

internal expect fun testDbDriver(): SqlDriver

fun athlete(imageUrl: String? = null) = Athlete(
    id = 123L,
    username = "jdamcd",
    name = "Jamie McDonald",
    imageUrl = imageUrl,
    isUser = true,
    lastUpdated = 456L
)

fun runStats() = RunStats(
    id = 123L,
    recentDistance = 100_000.0f,
    recentPace = 333,
    yearDistance = 1_000_000.0f,
    yearPace = 333,
    allDistance = 5_000_000.0f,
    allPace = 333,
    lastUpdated = 456L
)

fun activity(id: Long = 123L) = Activity(
    id = id,
    name = "Morning Eun",
    isPrivate = false,
    type = "RUN",
    subtype = "DEFAULT",
    distance = 10_000.0f,
    duration = 3330,
    pace = 333,
    start = "2020-01-01T00:00:00Z",
    mapPolyline = "polylineABCXYZ",
    lastUpdated = 456L
)
