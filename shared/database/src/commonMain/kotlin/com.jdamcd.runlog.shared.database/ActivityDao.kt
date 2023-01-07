package com.jdamcd.runlog.shared.database

class ActivityDao(database: RunLogDB) {

    private val activityQueries = database.activityQueries

    fun insert() {
        activityQueries.insert(id = 1L, name = "test")
    }
}
