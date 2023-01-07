package com.jdamcd.runlog.shared.database

import co.touchlab.sqliter.DatabaseConfiguration
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import com.squareup.sqldelight.drivers.native.wrapConnection

internal actual fun testDbDriver(): SqlDriver {
    return NativeSqliteDriver(
        DatabaseConfiguration(
            create = { connection ->
                wrapConnection(connection) { RunLogDB.Schema.create(it) }
            },
            name = "runlog.db",
            version = RunLogDB.Schema.version,
            inMemory = true
        )
    )
}
