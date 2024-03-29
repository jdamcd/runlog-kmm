package com.jdamcd.runlog.android.util

import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.ActivitySubtype
import com.jdamcd.runlog.shared.ActivityType
import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.AthleteStats
import com.jdamcd.runlog.shared.ImageUrl

val activityCard1 = ActivityCard(
    id = 123,
    name = "cool run",
    type = ActivityType.RUN,
    subtype = ActivitySubtype.RACE,
    distance = "10.1k",
    duration = "40:00",
    pace = "3:58 /km",
    start = "TUESDAY 25 OCT @ 5:58PM",
    mapUrl = ImageUrl("map.url/123")
)

val activityCard2 = ActivityCard(
    id = 456,
    name = "cooler run",
    type = ActivityType.RUN,
    subtype = ActivitySubtype.RACE,
    distance = "10.1k",
    duration = "40:00",
    pace = "3:58 /km",
    start = "TUESDAY 25 OCT @ 5:58PM",
    mapUrl = ImageUrl("map.url/123")
)

val athleteProfile = AthleteProfile(
    id = 123,
    username = "jdamcd",
    name = "Jamie McDonald",
    imageUrl = "image.url/123",
    recentRuns = AthleteStats(
        distance = "123km",
        pace = "5:00 /km"
    ),
    yearRuns = AthleteStats(
        distance = "1234km",
        pace = "5:00 /km"
    ),
    allRuns = AthleteStats(
        distance = "12345km",
        pace = "5:00 /km"
    )
)
