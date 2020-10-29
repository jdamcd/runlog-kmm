package com.jdamcd.runlog.shared

import com.jdamcd.runlog.shared.internal.StravaApi
import com.jdamcd.runlog.shared.internal.StravaInteractor

object SharedModule {
    fun buildStrava(userState: UserState): Strava = StravaInteractor(StravaApi(userState))
}
