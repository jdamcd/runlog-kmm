package com.jdamcd.runlog.shared.activity

import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.ActivityDetails
import com.jdamcd.runlog.shared.StravaActivity
import com.jdamcd.runlog.shared.api.StravaUrl
import com.jdamcd.runlog.shared.util.Result
import com.jdamcd.runlog.shared.util.tryCall

internal class ActivityInteractor(
    private val repo: ActivityRepository
) : StravaActivity {

    override suspend fun activities(): Result<List<ActivityCard>> = tryCall {
        repo.refresh()
        repo.activities()
    }

    override suspend fun activityDetails(id: Long): Result<ActivityDetails> = tryCall {
        repo.activityDetails(id)
    }

    override fun linkUrl(id: Long) = StravaUrl.linkUrl(id)
}
