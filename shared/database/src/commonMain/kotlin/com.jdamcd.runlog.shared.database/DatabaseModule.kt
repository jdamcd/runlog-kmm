package com.jdamcd.runlog.shared.database

import org.koin.core.module.Module
import org.koin.dsl.module

fun dbModule() = module {
    includes(platformDbModule())
    single { ActivityDao(get()) }
    single<AthleteDao> { SqlAthleteDao(get()) }
    single { DatabaseUtil(get(), get()) }
}

internal expect fun platformDbModule(): Module
