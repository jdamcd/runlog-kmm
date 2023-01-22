package com.jdamcd.runlog.shared.database

import com.squareup.sqldelight.android.AndroidSqliteDriver
import org.koin.dsl.module

actual fun platformDbModule() = module {
    single {
        val driver = AndroidSqliteDriver(RunLogDB.Schema, get(), "runlog.db")
        RunLogDB(driver)
    }
}
