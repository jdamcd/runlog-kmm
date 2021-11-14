package com.jdamcd.runlog.android.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.jdamcd.runlog.android.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.extras?.containsKey(EXTRA_CLEAR_USER) == true) {
            viewModel.signOut()
        }
        setupUi()
        listenForSuccess()
    }

    private fun setupUi() {
        setContent {
            Login(viewModel.uiModel) {
                viewModel.startLogin(this)
            }
        }
    }

    private fun listenForSuccess() {
        viewModel.uiModel.observe(this) {
            if (it is LoginState.Success) {
                startActivity(MainActivity.create(this))
                finish()
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

    companion object {
        fun create(context: Context) = Intent(context, LoginActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        fun clearUser(context: Context) = create(context).putExtra(EXTRA_CLEAR_USER, true)

        private const val CODE_PARAM = "code"
        private const val EXTRA_CLEAR_USER = "clear_user"
    }
}
