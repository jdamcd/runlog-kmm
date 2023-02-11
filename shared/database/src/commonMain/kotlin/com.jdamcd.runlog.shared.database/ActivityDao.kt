package com.jdamcd.runlog.shared.database

interface ActivityDao : Dao {
    override fun clear()
}

class SqlActivityDao(database: RunLogDB) : ActivityDao {

    private val queries = database.activityQueries

    override fun clear() {
        queries.deleteAll()
    }
}
