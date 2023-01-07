package com.jdamcd.runlog.shared.database

import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import org.koin.dsl.module

actual fun databaseModule() = module {
    single {
        val driver = NativeSqliteDriver(RunLogDB.Schema, "runlog.db")
        RunLogDB(driver)
    }
}
