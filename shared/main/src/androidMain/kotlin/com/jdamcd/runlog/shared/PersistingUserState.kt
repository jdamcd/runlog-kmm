package com.jdamcd.runlog.shared

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

actual class PersistingUserState(context: Context) : UserState {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    actual override var accessToken: String
        get() = prefs.getString(ACCESS_TOKEN, "")!!
        set(accessToken) = prefs.edit { putString(ACCESS_TOKEN, accessToken) }

    actual override var refreshToken: String
        get() = prefs.getString(REFRESH_TOKEN, "")!!
        set(refreshToken) = prefs.edit { putString(REFRESH_TOKEN, refreshToken) }

    actual override fun isLoggedIn() = accessToken.isNotEmpty()

    actual override fun clear() {
        prefs.edit { clear() }
    }

    companion object {
        private const val PREFS_NAME = "user_prefs"
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
    }
}
