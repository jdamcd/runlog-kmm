package com.jdamcd.runlog.shared.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import comjdamcdrunlogshareddatabase.Athlete
import comjdamcdrunlogshareddatabase.AthleteWithStats
import comjdamcdrunlogshareddatabase.RunStats
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow

interface AthleteDao : Dao {
    fun insert(athlete: Athlete)
    fun insert(athlete: Athlete, runStats: RunStats)
    fun user(): AthleteWithStats?
    fun userImageUrl(): String?
    fun userFlow(): Flow<AthleteWithStats?>
    override fun clear()
}

internal class SqlAthleteDao(database: RunLogDB) : AthleteDao {

    private val queries = database.athleteQueries

    override fun insert(athlete: Athlete) {
        queries.insertAthlete(athlete)
    }

    override fun insert(athlete: Athlete, runStats: RunStats) {
        queries.transaction {
            queries.insertAthlete(athlete)
            queries.insertRunStats(runStats)
        }
    }

    override fun user(): AthleteWithStats? = queries.selectUser()
        .executeAsOneOrNull()

    override fun userImageUrl(): String? = queries.selectUserImageUrl()
        .executeAsOneOrNull()?.imageUrl

    override fun userFlow(): Flow<AthleteWithStats?> = queries.selectUser()
        .asFlow()
        .mapToOneOrNull(Dispatchers.IO)

    override fun clear() {
        queries.deleteAll()
    }
}
