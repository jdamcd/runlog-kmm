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

    suspend fun refresh(): RefreshState {
        return try {
            dao.insert(api.activities().map { mapper.summaryApiToDb(it) })
            log.debug("Activities refreshed successfully")
            RefreshState.SUCCESS
        } catch (e: Exception) {
            log.error("Activities refresh failed: ${e.message}")
            RefreshState.ERROR
        }
    }

    suspend fun activities(): Result<List<ActivityCard>> {
        return withContext(Dispatchers.Default) {
            dao.allActivities().map { mapper.summaryDbToUi(it) }.let {
                if (it.isEmpty()) Result.Empty else Result.Data(it)
            }
        }
    }

    fun activitiesFlow(): Flow<Result<List<ActivityCard>>> {
        return dao.allActivitiesFlow().map { list ->
            list.map { mapper.summaryDbToUi(it) }.let { mapped ->
                if (mapped.isEmpty()) Result.Empty else Result.Data(mapped)
            }
        }
    }

    suspend fun activityDetails(id: Long): Result<ActivityDetails> {
        val activityDetails = api.activity(id)
        return Result.Data(mapper.detailApiToUi(activityDetails))
    }
}
