package com.jdamcd.runlog.shared

import com.jdamcd.runlog.shared.api.TokenProvider
import com.jdamcd.runlog.shared.login.PersistingUserState
import org.koin.dsl.binds
import org.koin.dsl.module

actual fun platformModule() = module {
    single { PersistingUserState() }
        .binds(arrayOf(PersistingUserState::class, TokenProvider::class))
}
