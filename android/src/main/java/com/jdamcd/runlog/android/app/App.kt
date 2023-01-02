package com.jdamcd.runlog.android.app

import android.app.Application
import com.jdamcd.runlog.shared.initKoin
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger()
            androidContext(this@App)
        }
    }
}
