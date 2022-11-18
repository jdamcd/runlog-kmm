package com.jdamcd.runlog.android.util

import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.ActivityType
import com.jdamcd.runlog.shared.AthleteProfile

val activityCard1 = ActivityCard(
    id = 123,
    name = "cool run",
    type = ActivityType.RUN,
    isRace = true,
    distance = "10.1k",
    duration = "40:00",
    pace = "3:58/k",
    start = "TUESDAY 25 OCT @ 5:58PM",
    mapUrl = "map.url/123"
)

val activityCard2 = ActivityCard(
    id = 456,
    name = "cooler run",
    type = ActivityType.RUN,
    isRace = true,
    distance = "10.1k",
    duration = "40:00",
    pace = "3:58/k",
    start = "TUESDAY 25 OCT @ 5:58PM",
    mapUrl = "map.url/123"
)

val athleteProfile = AthleteProfile(
    id = 123,
    username = "jdamcd",
    name = "Jamie McDonald",
    imageUrl = "image.url/123",
    yearRunDistance = "123km",
    allTimeRunDistance = "1,234km"
)
