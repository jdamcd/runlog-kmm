package com.jdamcd.runlog.shared.database

class ActivityDao(database: RunLogDB) {

    private val queries = database.activityQueries

    internal fun clear() {
        queries.deleteAll()
    }
}
