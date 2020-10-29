package com.jdamcd.runlog.android.login

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdamcd.runlog.shared.LoginResult
import com.jdamcd.runlog.shared.Strava
import com.jdamcd.runlog.shared.UserState
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val strava: Strava,
    private val userState: UserState
) : ViewModel(), LifecycleObserver {

    private val liveData = MutableLiveData<LoginState>(LoginState.Idle)
    val uiModel = liveData as LiveData<LoginState>

    fun startLogin(context: Context) {
        liveData.value = LoginState.Loading
        val intent = Intent(Intent.ACTION_VIEW, strava.loginUrl.toUri())
        context.startActivity(intent)
    }

    fun submitAuthCode(code: String) {
        liveData.value = LoginState.Loading
        viewModelScope.launch {
            when (strava.authenticate(code)) {
                is LoginResult.Success -> liveData.value = LoginState.Success
                is LoginResult.Error -> liveData.value = LoginState.Idle
            }
        }
    }

    fun signOut() = userState.clear()
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
}
