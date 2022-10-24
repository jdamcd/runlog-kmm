package com.jdamcd.runlog.android.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jdamcd.runlog.android.login.LoginActivity
import com.jdamcd.runlog.android.main.MainActivity
import com.jdamcd.runlog.shared.UserState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LaunchActivity : AppCompatActivity() {

    @Inject lateinit var userState: UserState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
