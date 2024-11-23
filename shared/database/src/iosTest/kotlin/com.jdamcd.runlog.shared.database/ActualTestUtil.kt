package com.jdamcd.runlog.shared.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import app.cash.sqldelight.driver.native.wrapConnection
import co.touchlab.sqliter.DatabaseConfiguration

internal actual fun testDbDriver(): SqlDriver = NativeSqliteDriver(
    DatabaseConfiguration(
        create = { connection ->
            wrapConnection(connection) { RunLogDB.Schema.create(it) }
        },
        name = "runlog.db",
        version = RunLogDB.Schema.version.toInt(),
        inMemory = true
    )
)
