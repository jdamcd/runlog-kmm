package com.jdamcd.runlog.shared.database

import com.squareup.sqldelight.runtime.coroutines.asFlow
import comjdamcdrunlogshareddatabase.Activity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ActivityDao : Dao {
    fun insert(activities: List<Activity>)
    fun allActivities(): List<Activity>
    fun allActivitiesFlow(): Flow<List<Activity>>
    override fun clear()
}

class SqlActivityDao(database: RunLogDB) : ActivityDao {

    private val queries = database.activityQueries

    override fun insert(activities: List<Activity>) {
        queries.transaction {
            activities.forEach { queries.insertActivity(it) }
        }
    }

    override fun allActivities() = queries.selectAll().executeAsList()

    override fun allActivitiesFlow() = queries.selectAll().asFlow().map {
        it.executeAsList()
    }

    override fun clear() {
        queries.deleteAll()
    }
}
