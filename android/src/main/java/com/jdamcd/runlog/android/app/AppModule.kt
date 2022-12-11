package com.jdamcd.runlog.android.app

import android.content.Context
import com.jdamcd.runlog.shared.PersistingUserState
import com.jdamcd.runlog.shared.SharedModule
import com.jdamcd.runlog.shared.UserState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class AppModule {

    @Provides
    @Singleton
    fun provideUserState(@ApplicationContext context: Context): UserState = PersistingUserState(context)

    @Provides
    fun provideStravaLogin(userState: UserState) = SharedModule.stravaLogin(userState)

    @Provides
    fun provideStravaActivity(userState: UserState) = SharedModule.stravaActivity(userState)

    @Provides
    fun provideStravaProfile(userState: UserState) = SharedModule.stravaProfile(userState)
}
