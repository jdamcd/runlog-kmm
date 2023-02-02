package com.jdamcd.runlog.shared.database

import comjdamcdrunlogshareddatabase.Athlete
import comjdamcdrunlogshareddatabase.RunStats

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
