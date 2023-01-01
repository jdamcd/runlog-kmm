package com.jdamcd.runlog.shared

import org.koin.dsl.module

actual fun platformModule() = module {
    single<UserState> { PersistingUserState() }
}