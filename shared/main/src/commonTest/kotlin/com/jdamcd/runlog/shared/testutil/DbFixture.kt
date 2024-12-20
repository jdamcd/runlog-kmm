package com.jdamcd.runlog.shared.testutil

import comjdamcdrunlogshareddatabase.AthleteWithStats

fun athleteDbModel() = AthleteWithStats(
    id = 123L,
    username = "jdamcd",
    name = "Jamie McDonald",
    imageUrl = "image.url",
    isUser = true,
    lastUpdated = 456L,
    recentDistance = 100_000.0,
    recentPace = 333,
    yearDistance = 1_000_000.0,
    yearPace = 333,
    allDistance = 5_000_000.0,
    allPace = 333
)
