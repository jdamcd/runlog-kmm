package com.jdamcd.runlog.shared.profile

import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.Result
import com.jdamcd.runlog.shared.StravaProfile
import com.jdamcd.runlog.shared.util.tryCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

internal class ProfileInteractor(
    private val repository: ProfileRepository
) : StravaProfile {

    private val coroutineScope: CoroutineScope = MainScope()

    override suspend fun profile(): Result<AthleteProfile> = tryCall {
        Result.Data(repository.profile())
    }

    override fun profileFlow(): Flow<AthleteProfile> {
        refresh()
        return repository.loadProfile()
    }

    override fun refresh() {
        coroutineScope.launch {
            repository.fetchProfile()
        }
    }
}
