package com.jdamcd.runlog.android.util

import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.AthleteProfile

val activityCard = ActivityCard(
    id = 123,
    name = "cool run",
    type = "run",
    label = null,
    distance = "10.3k",
    duration = "40:00",
    start = "Tuesday 25 Oct @ 5:58pm",
    mapUrl = null
)

val athleteProfile = AthleteProfile(
    id = 123,
    username = "jdamcd",
    name = "Jamie McDonald",
    imageUrl = "image.url/123",
    yearRunDistance = "123km",
    allTimeRunDistance = "1,234km"
)
