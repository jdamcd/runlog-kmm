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

    // TODO: Separate read and write paths
    override suspend fun activities(): Result<List<ActivityCard>> = tryCall {
        val result = api.activities()
        dao.insert(result.map { mapper.activityToDb(it) })

        Result.Data(result.map { mapper.mapActivityCard(it) })
    }

    override suspend fun activityDetails(id: Long): Result<ActivityDetails> = tryCall {
        val activityDetails = api.activity(id)
        Result.Data(mapper.mapActivityDetails(activityDetails))
    }

    override fun linkUrl(id: Long) = StravaUrl.linkUrl(id)

    override fun requestDarkModeImages(enabled: Boolean) {
        mapper.darkModeImages = enabled
    }
}
