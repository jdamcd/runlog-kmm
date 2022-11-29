package com.jdamcd.runlog.android.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.net.toUri
import com.jdamcd.runlog.android.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainNavigation(
                openLink = { openLink(this, it) },
                signOut = { signOut() }
            )
        }
    }

    private fun signOut() {
        startActivity(LoginActivity.clearUser(this))
        finish()
    }

    private fun openLink(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        context.startActivity(intent)
    }

    companion object {
        fun create(context: Context) = Intent(context, MainActivity::class.java)
    }
}
