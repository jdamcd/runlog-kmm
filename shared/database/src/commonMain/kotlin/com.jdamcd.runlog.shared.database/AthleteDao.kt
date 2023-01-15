package com.jdamcd.runlog.shared.database

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import comjdamcdrunlogshareddatabase.Athlete
import comjdamcdrunlogshareddatabase.AthleteWithStats
import comjdamcdrunlogshareddatabase.RunStats
import kotlinx.coroutines.flow.Flow

class AthleteDao(database: RunLogDB) {

    private val queries = database.athleteQueries

    fun insert(athlete: Athlete, runStats: RunStats) {
        queries.transaction {
            queries.insertAthlete(athlete)
            queries.insertRunStats(runStats)
        }
    }

    fun user(): AthleteWithStats? {
        return queries.selectUser()
            .executeAsOneOrNull()
    }

    fun userFlow(): Flow<AthleteWithStats?> {
        return queries.selectUser()
            .asFlow()
            .mapToOneOrNull()
    }

    internal fun clear() {
        queries.deleteAll()
    }
}
