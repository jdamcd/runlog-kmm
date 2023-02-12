package com.jdamcd.runlog.shared.database

import comjdamcdrunlogshareddatabase.Activity

interface ActivityDao : Dao {
    fun insert(activity: Activity)
    fun allActivities(): List<Activity>
    override fun clear()
}

class SqlActivityDao(database: RunLogDB) : ActivityDao {

    private val queries = database.activityQueries

    override fun insert(activity: Activity) {
        queries.insertActivity(activity)
    }

    override fun allActivities(): List<Activity> {
        return queries.selectAll().executeAsList()
    }

    override fun clear() {
        queries.deleteAll()
    }
}
