package com.jdamcd.runlog.shared.database

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseUtil(
    private val athleteDao: AthleteDao,
    private val activityDao: ActivityDao
) {

    suspend fun clear() {
        withContext(Dispatchers.Default) {
            athleteDao.clear()
            activityDao.clear()
        }
    }
}

interface Dao {
    fun clear()
}
