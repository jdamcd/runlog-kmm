package com.jdamcd.runlog.android.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jdamcd.runlog.android.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUi()
        savedInstanceState.takeIf { it != null }.let {
            viewModel.load()
        }
        listenForErrors()
    }

    private fun listenForErrors() {
        viewModel.uiModel.observe(this) {
            if (it is ActivityFeedState.Error && !it.recoverable) {
                signOut()
            }
        }
    }

    private fun signOut() {
        startActivity(LoginActivity.clearUser(this))
        finish()
    }

    private fun setupUi() {
        setContent {
            ActivityFeed(
                liveData = viewModel.uiModel,
                onItemClick = { id -> viewModel.openLink(this, id) },
                onRetryClick = { viewModel.load() },
                onSignOutClick = { signOut() }
            )
        }
    }

    companion object {
        fun create(context: Context) = Intent(context, MainActivity::class.java)
    }
}
