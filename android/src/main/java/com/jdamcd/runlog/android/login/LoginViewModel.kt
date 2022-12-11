package com.jdamcd.runlog.android.login

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jdamcd.runlog.shared.LoginResult
import com.jdamcd.runlog.shared.StravaLogin
import com.jdamcd.runlog.shared.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val stravaLogin: StravaLogin,
    private val userState: UserState
) : ViewModel(), LifecycleObserver {

    private val _mutableFlow = MutableStateFlow<LoginState>(LoginState.Idle)
    val flow = _mutableFlow as StateFlow<LoginState>

    fun startLogin(): String {
        _mutableFlow.value = LoginState.Loading
        return stravaLogin.loginUrl
    }

    fun submitAuthCode(code: String) {
        _mutableFlow.value = LoginState.Loading
        viewModelScope.launch {
            when (stravaLogin.authenticate(code)) {
                is LoginResult.Success -> _mutableFlow.value = LoginState.Success
                is LoginResult.Error -> _mutableFlow.value = LoginState.Idle
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
