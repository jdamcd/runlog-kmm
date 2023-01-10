package com.jdamcd.runlog.shared.database

import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import comjdamcdrunlogshareddatabase.Athlete
import comjdamcdrunlogshareddatabase.AthleteWithStats
import comjdamcdrunlogshareddatabase.RunStats
import kotlinx.coroutines.flow.Flow

class AthleteDao(database: RunLogDB) {

    private val athleteQueries = database.athleteQueries

    fun insert(athlete: Athlete, runStats: RunStats) {
        athleteQueries.transaction {
            athleteQueries.insertAthlete(athlete)
            athleteQueries.insertRunStats(runStats)
        }
    }

    fun user(): AthleteWithStats {
        return athleteQueries.selectUser().executeAsOne()
    }

    fun userFlow(): Flow<AthleteWithStats?> {
        return athleteQueries.selectUser()
            .asFlow()
            .mapToOneOrNull()
    }
}
