package com.jdamcd.runlog.shared.api

import org.koin.dsl.module

fun apiModule() = module {
    single<StravaApi> { KtorStravaApi(get()) }
}
