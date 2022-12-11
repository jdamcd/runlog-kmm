package com.jdamcd.runlog.shared

interface StravaLogin {
    suspend fun authenticate(code: String): LoginResult
    val loginUrl: String
    val authScheme: String
}

sealed class LoginResult {
    object Success : LoginResult()
    data class Error(val error: Throwable) : LoginResult()
}
