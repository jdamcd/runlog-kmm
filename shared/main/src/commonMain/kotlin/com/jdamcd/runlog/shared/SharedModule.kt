package com.jdamcd.runlog.shared

import com.jdamcd.runlog.shared.api.StravaApi
import com.jdamcd.runlog.shared.api.TokenProvider
import com.jdamcd.runlog.shared.internal.ActivityInteractor
import com.jdamcd.runlog.shared.internal.ActivityMapper
import com.jdamcd.runlog.shared.internal.LoginInteractor
import com.jdamcd.runlog.shared.internal.ProfileInteractor
import com.jdamcd.runlog.shared.internal.ProfileMapper
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(commonModule(), platformModule())
}

fun commonModule() = module {
    single { StravaApi(get()) }
    single<StravaLogin> { LoginInteractor(get()) }
    single<StravaActivity> { ActivityInteractor(get(), ActivityMapper()) }
    single<StravaProfile> { ProfileInteractor(get(), ProfileMapper()) }
}

expect fun platformModule(): Module

// Called from iOS
@Suppress("unused")
fun initKoin() = initKoin {}

@Suppress("unused")
class IosDI : KoinComponent {
    fun userState(): UserState = get()
    fun stravaLogin(): StravaLogin = get()
    fun stravaActivity(): StravaActivity = get()
    fun stravaProfile(): StravaProfile = get()
}
