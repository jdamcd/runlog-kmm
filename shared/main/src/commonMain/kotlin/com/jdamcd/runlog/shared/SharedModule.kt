package com.jdamcd.runlog.shared

import com.jdamcd.runlog.shared.activity.ActivityInteractor
import com.jdamcd.runlog.shared.activity.ActivityMapper
import com.jdamcd.runlog.shared.activity.ActivityRepository
import com.jdamcd.runlog.shared.api.apiModule
import com.jdamcd.runlog.shared.database.dbModule
import com.jdamcd.runlog.shared.login.LoginInteractor
import com.jdamcd.runlog.shared.profile.AthleteMapper
import com.jdamcd.runlog.shared.profile.ProfileRepository
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(
        commonModule(),
        platformModule(),
        dbModule(),
        apiModule()
    )
}

fun commonModule() = module {
    single<Clock> { Clock.System }
    single { ActivityMapper(get()) }
    single { AthleteMapper(get()) }
    single<StravaLogin> { LoginInteractor(get(), get(), get()) }
    single { ActivityRepository(get(), get(), get()) }
    single<StravaActivity> { ActivityInteractor(get()) }
    single<StravaProfile> { ProfileRepository(get(), get(), get()) }
    single<UserManager> { PersistingUserManager(get(), get()) }
}

expect fun platformModule(): Module

// Called from iOS
@Suppress("unused")
fun initKoin() = initKoin {}

@Suppress("unused")
class IosDI : KoinComponent {
    fun userManager(): UserManager = get()
    fun stravaLogin(): StravaLogin = get()
    fun stravaActivity(): StravaActivity = get()
    fun stravaProfile(): StravaProfile = get()
}
