package com.jdamcd.runlog.shared.database

import comjdamcdrunlogshareddatabase.Athlete
import comjdamcdrunlogshareddatabase.AthleteWithStats
import comjdamcdrunlogshareddatabase.RunStats

class AthleteDao(database: RunLogDB) {

    private val athleteQueries = database.athleteQueries

    fun insert(athlete: Athlete, runStats: RunStats) {
        athleteQueries.transaction {
            athleteQueries.insertAthlete(athlete)
            athleteQueries.insertRunStats(runStats)
        }
    }

    fun getUser(): AthleteWithStats {
        return athleteQueries.selectUser().executeAsOne()
    }
}
