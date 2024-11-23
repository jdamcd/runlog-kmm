package com.jdamcd.runlog.shared.database

import app.cash.sqldelight.db.SqlDriver
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
    recentDistance = 100_000.0,
    recentPace = 333,
    yearDistance = 1_000_000.0,
    yearPace = 333,
    allDistance = 5_000_000.0,
    allPace = 333,
    lastUpdated = 456L
)

fun activity(id: Long = 123L, isPrivate: Boolean = false) = Activity(
    id = id,
    name = "Morning Run",
    isPrivate = isPrivate,
    type = "RUN",
    subtype = "DEFAULT",
    distance = 10_000.0,
    duration = 3330,
    pace = 333,
    start = "2020-01-01T00:00:00Z",
    mapPolyline = "polylineABCXYZ",
    lastUpdated = 456L
)
