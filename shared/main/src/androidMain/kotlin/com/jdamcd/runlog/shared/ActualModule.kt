package com.jdamcd.runlog.shared

import com.jdamcd.runlog.shared.api.TokenProvider
import org.koin.dsl.binds
import org.koin.dsl.module

actual fun platformModule() = module {
    single { PersistingUserState(get()) }
        .binds(arrayOf(UserState::class, TokenProvider::class))
}
