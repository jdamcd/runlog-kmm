package com.jdamcd.runlog.shared

import com.jdamcd.runlog.shared.database.DatabaseUtil
import com.jdamcd.runlog.shared.login.PersistingUserState
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

interface UserManager {
    fun isLoggedIn(): Boolean
    fun logOut()
}

class PersistingUserManager internal constructor(
    private val userState: PersistingUserState,
    private val database: DatabaseUtil
) : UserManager {

    override fun isLoggedIn() = userState.isLoggedIn()

    @OptIn(DelicateCoroutinesApi::class)
    override fun logOut() {
        // Fire and forget to avoid blocking logout
        GlobalScope.launch {
            userState.clear()
            database.clear()
        }
    }
}
