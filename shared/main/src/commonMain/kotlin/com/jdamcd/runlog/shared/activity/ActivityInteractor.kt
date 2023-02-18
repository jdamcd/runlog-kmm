package com.jdamcd.runlog.shared.activity

import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.ActivityDetails
import com.jdamcd.runlog.shared.StravaActivity
import com.jdamcd.runlog.shared.api.StravaApi
import com.jdamcd.runlog.shared.api.StravaUrl
import com.jdamcd.runlog.shared.database.ActivityDao
import com.jdamcd.runlog.shared.util.Result
import com.jdamcd.runlog.shared.util.tryCall

internal class ActivityInteractor(
    private val api: StravaApi,
    private val dao: ActivityDao,
    private val mapper: ActivityMapper
) : StravaActivity {

    var darkMode = false

    // TODO: Separate read and write paths
    override suspend fun activities(): Result<List<ActivityCard>> = tryCall {
        dao.insert(api.activities().map { mapper.summaryApiToDb(it) })
        Result.Data(dao.allActivities().map { mapper.summaryDbToUi(it, darkMode) })
    }

    override suspend fun activityDetails(id: Long): Result<ActivityDetails> = tryCall {
        val activityDetails = api.activity(id)
        Result.Data(mapper.detailApiToUi(activityDetails, darkMode))
    }

    override fun linkUrl(id: Long) = StravaUrl.linkUrl(id)

    override fun requestDarkModeImages(enabled: Boolean) {
        darkMode = enabled
    }
}
