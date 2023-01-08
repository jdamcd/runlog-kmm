package com.jdamcd.runlog.shared.profile

import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.Result
import com.jdamcd.runlog.shared.StravaProfile
import com.jdamcd.runlog.shared.util.tryCall

internal class ProfileInteractor(
    private val repository: ProfileRepository
) : StravaProfile {

    override suspend fun profile(): Result<AthleteProfile> = tryCall {
        Result.Data(repository.profile())
    }
}
