package com.jdamcd.runlog.android.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.jdamcd.runlog.android.login.LoginActivity
import com.jdamcd.runlog.android.main.MainActivity
import com.jdamcd.runlog.shared.UserState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LaunchActivity : ComponentActivity() {

    @Inject lateinit var userState: UserState

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition { true }
        startActivity(
            if (userState.isLoggedIn()) {
                MainActivity.create(this)
            } else {
                LoginActivity.create(this)
            }
        )
        finish()
    }
}
