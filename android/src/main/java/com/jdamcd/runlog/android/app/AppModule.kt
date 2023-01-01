package com.jdamcd.runlog.android.app

import com.jdamcd.runlog.shared.StravaActivity
import com.jdamcd.runlog.shared.StravaLogin
import com.jdamcd.runlog.shared.StravaProfile
import com.jdamcd.runlog.shared.UserState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

/*
 * Bridge between Koin in the shared multiplatform modules and Hilt in the Android project.
 */

@Module
@InstallIn(SingletonComponent::class)
internal class AppModule : KoinComponent {

    @Provides
    fun provideUserState(): UserState = get()

    @Provides
    fun provideStravaLogin(): StravaLogin = get()

    @Provides
    fun provideStravaActivity(): StravaActivity = get()

    @Provides
    fun provideStravaProfile(): StravaProfile = get()
}
