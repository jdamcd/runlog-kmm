package com.jdamcd.runlog.shared.database

import app.cash.sqldelight.coroutines.asFlow
import comjdamcdrunlogshareddatabase.Activity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface ActivityDao : Dao {
    fun insert(activities: List<Activity>)
    fun latestActivities(): List<Activity>
    fun latestActivitiesFlow(): Flow<List<Activity>>
    override fun clear()
}

class SqlActivityDao(database: RunLogDB) : ActivityDao {

    private val queries = database.activityQueries

    override fun insert(activities: List<Activity>) {
        queries.transaction {
            activities.forEach { queries.insertActivity(it) }
        }
    }

    override fun latestActivities() = queries.selectLatest().executeAsList()

    override fun latestActivitiesFlow() = queries.selectLatest().asFlow().map {
        it.executeAsList()
    }

    override fun clear() {
        queries.deleteAll()
    }
}
