package com.jdamcd.runlog.shared.login

import com.jdamcd.runlog.shared.api.TokenProvider
import platform.Foundation.NSUserDefaults
import platform.Foundation.setValue

internal actual class PersistingUserState : TokenProvider {

    private val defaults: NSUserDefaults = NSUserDefaults(suiteName = DEFAULTS_NAME)

    actual override var accessToken: String
        get() = defaults.stringForKey(ACCESS_TOKEN) ?: ""
        set(accessToken) = defaults.setValue(value = accessToken, forKey = ACCESS_TOKEN)

    actual override var refreshToken: String
        get() = defaults.stringForKey(REFRESH_TOKEN) ?: ""
        set(refreshToken) = defaults.setValue(value = refreshToken, forKey = REFRESH_TOKEN)

    actual override fun isLoggedIn() = accessToken.isNotEmpty()

    actual fun clear() {
        defaults.removeObjectForKey(ACCESS_TOKEN)
        defaults.removeObjectForKey(REFRESH_TOKEN)
    }

    companion object {
        private const val DEFAULTS_NAME = "user_defaults"
        private const val ACCESS_TOKEN = "access_token"
        private const val REFRESH_TOKEN = "refresh_token"
    }
}
