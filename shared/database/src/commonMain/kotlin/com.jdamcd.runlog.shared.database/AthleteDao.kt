package com.jdamcd.runlog.shared.database

class AthleteDao(database: RunLogDB) {

    private val athleteQueries = database.athleteQueries

    fun insert() {
        athleteQueries.insert(id = 1L, name = "test")
    }
}
