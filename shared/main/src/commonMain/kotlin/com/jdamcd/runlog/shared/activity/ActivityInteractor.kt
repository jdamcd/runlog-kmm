package com.jdamcd.runlog.shared.activity

import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.ActivityDetails
import com.jdamcd.runlog.shared.Result
import com.jdamcd.runlog.shared.StravaActivity
import com.jdamcd.runlog.shared.api.StravaApi
import com.jdamcd.runlog.shared.util.tryCall

internal class ActivityInteractor(
    private val stravaApi: StravaApi,
    private val mapper: ActivityMapper
) : StravaActivity {

    override suspend fun activities(): Result<List<ActivityCard>> = tryCall {
        val activities = stravaApi.activities().map {
            mapper.mapActivityCard(it)
        }
        Result.Data(activities)
    }

    override suspend fun activityDetails(id: Long): Result<ActivityDetails> = tryCall {
        val activityDetails = stravaApi.activity(id)
        Result.Data(mapper.mapActivityDetails(activityDetails))
    }

    override fun linkUrl(id: Long) = StravaApi.linkUrl(id)

    override fun requestDarkModeImages(enabled: Boolean) {
        mapper.darkModeImages = enabled
    }
}