package com.jdamcd.runlog.android.app

import android.content.Context
import com.jdamcd.runlog.shared.SharedModule
import com.jdamcd.runlog.shared.UserState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
internal class AppModule {

    @Provides
    fun provideUserState(@ApplicationContext context: Context): UserState = UserState(context)

    @Provides
    fun provideStrava(userState: UserState) = SharedModule.buildStrava(userState)
}
