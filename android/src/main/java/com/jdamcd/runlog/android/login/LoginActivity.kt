package com.jdamcd.runlog.android.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.jdamcd.runlog.android.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.extras?.containsKey(EXTRA_CLEAR_USER) == true) {
            viewModel.signOut()
        }
        setContent {
            Login(
                viewModel = viewModel,
                onOpenLink = { openLink(this, it) }
            )
        }
        listenForSuccess()
    }

    private fun listenForSuccess() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.flow.collect {
                    if (it is LoginState.Success) {
                        startActivity(MainActivity.create(this@LoginActivity))
                        finish()
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    override fun onResume() {
        super.onResume()

        val authCode = intent.data?.getQueryParameter(CODE_PARAM)
        authCode.takeIf { !it.isNullOrEmpty() }?.let {
            viewModel.submitAuthCode(it)
            intent.data = null
        }
    }

    private fun openLink(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        context.startActivity(intent)
    }

    companion object {
        fun create(context: Context) = Intent(context, LoginActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        fun clearUser(context: Context) = create(context).putExtra(EXTRA_CLEAR_USER, true)

        private const val CODE_PARAM = "code"
        private const val EXTRA_CLEAR_USER = "clear_user"
    }
}
