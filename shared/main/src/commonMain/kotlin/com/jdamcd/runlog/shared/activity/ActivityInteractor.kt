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

    var darkMode = false

    override suspend fun activities(): Result<List<ActivityCard>> = tryCall {
        repo.activities(darkMode)
    }

    override suspend fun activityDetails(id: Long): Result<ActivityDetails> = tryCall {
        repo.activityDetails(id, darkMode)
    }

    override fun linkUrl(id: Long) = StravaUrl.linkUrl(id)

    override fun requestDarkModeImages(enabled: Boolean) {
        darkMode = enabled
    }
}
