package com.jdamcd.runlog.shared.database

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import org.koin.dsl.module

internal actual fun platformDbModule() = module {
    single {
        val driver = NativeSqliteDriver(RunLogDB.Schema, "runlog.db")
        RunLogDB(driver)
    }
}
