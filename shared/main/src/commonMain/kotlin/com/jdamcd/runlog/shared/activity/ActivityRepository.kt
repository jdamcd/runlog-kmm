package com.jdamcd.runlog.shared.activity

import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.ActivityDetails
import com.jdamcd.runlog.shared.api.StravaApi
import com.jdamcd.runlog.shared.database.ActivityDao
import com.jdamcd.runlog.shared.util.Result

internal class ActivityRepository(
    private val api: StravaApi,
    private val dao: ActivityDao,
    private val mapper: ActivityMapper
) {

    // TODO: Separate read and write paths
    suspend fun activities(darkMode: Boolean): Result<List<ActivityCard>> {
        dao.insert(api.activities().map { mapper.summaryApiToDb(it) })
        return Result.Data(dao.allActivities().map { mapper.summaryDbToUi(it, darkMode) })
    }

    suspend fun activityDetails(id: Long, darkMode: Boolean): Result<ActivityDetails> {
        val activityDetails = api.activity(id)
        return Result.Data(mapper.detailApiToUi(activityDetails, darkMode))
    }
}
