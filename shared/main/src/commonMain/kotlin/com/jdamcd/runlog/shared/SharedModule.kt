package com.jdamcd.runlog.shared

import com.jdamcd.runlog.shared.activity.ActivityInteractor
import com.jdamcd.runlog.shared.activity.ActivityMapper
import com.jdamcd.runlog.shared.api.StravaApi
import com.jdamcd.runlog.shared.database.ActivityDao
import com.jdamcd.runlog.shared.database.AthleteDao
import com.jdamcd.runlog.shared.database.DatabaseUtil
import com.jdamcd.runlog.shared.database.databaseModule
import com.jdamcd.runlog.shared.login.LoginInteractor
import com.jdamcd.runlog.shared.profile.ProfileMapper
import com.jdamcd.runlog.shared.profile.ProfileRepository
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
        databaseModule()
    )
}

fun commonModule() = module {
    single { StravaApi(get()) }
    single<StravaLogin> { LoginInteractor(get()) }
    single { ActivityDao(get()) }
    single<StravaActivity> { ActivityInteractor(get(), ActivityMapper()) }
    single { AthleteDao(get()) }
    single<StravaProfile> { ProfileRepository(get(), get(), ProfileMapper()) }
    single { DatabaseUtil(get(), get()) }
    single<UserManager> { UserManagerImpl(get(), get()) }
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
