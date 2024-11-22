package com.jdamcd.runlog.shared.activity

import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.ActivityDetails
import com.jdamcd.runlog.shared.api.StravaApi
import com.jdamcd.runlog.shared.database.ActivityDao
import com.jdamcd.runlog.shared.util.Log
import com.jdamcd.runlog.shared.util.MultiLog
import com.jdamcd.runlog.shared.util.RefreshState
import com.jdamcd.runlog.shared.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class ActivityRepository(
    private val api: StravaApi,
    private val dao: ActivityDao,
    private val mapper: ActivityMapper,
    private val log: Log = MultiLog()
) {

    suspend fun refresh(): RefreshState = try {
        if (dao.latestActivities().isEmpty()) {
            fullSync()
        } else {
            dao.insert(api.activities(pageSize = 20).map { mapper.summaryApiToDb(it) })
        }
        log.debug("Activities refreshed successfully")
        RefreshState.SUCCESS
    } catch (e: Exception) {
        log.error("Activities refresh failed: ${e.message}")
        RefreshState.ERROR
    }

    suspend fun fullSync() {
        log.debug("Starting full activity sync")
        var page = 1
        var activities = api.activities(page = page, pageSize = 200)
        while (activities.isNotEmpty()) {
            dao.insert(activities.map { mapper.summaryApiToDb(it) })
            activities = api.activities(page = ++page, pageSize = 200)
            log.debug("Fetched page $page, containing ${activities.size} activities")
        }
    }

    suspend fun activities(): Result<List<ActivityCard>> = withContext(Dispatchers.Default) {
        dao.latestActivities().map { mapper.summaryDbToUi(it) }.let {
            if (it.isEmpty()) Result.Empty else Result.Data(it)
        }
    }

    fun activitiesFlow(): Flow<List<ActivityCard>> = dao.latestActivitiesFlow().map { list ->
        list.map { mapper.summaryDbToUi(it) }
    }

    suspend fun activityDetails(id: Long): Result<ActivityDetails> {
        val activityDetails = api.activity(id)
        return Result.Data(mapper.detailApiToUi(activityDetails))
    }
}
