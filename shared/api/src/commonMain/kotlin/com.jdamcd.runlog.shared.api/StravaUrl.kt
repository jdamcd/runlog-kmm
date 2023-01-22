package com.jdamcd.runlog.shared.api

import io.ktor.http.URLBuilder

object StravaUrl {

    const val AUTH_SCHEME = "km-auth"

    fun loginUrl(clientId: String = BuildKonfig.CLIENT_ID) =
        URLBuilder("https://strava.com/oauth/mobile/authorize").apply {
            parameters.apply {
                append("client_id", clientId)
                append("redirect_uri", "$AUTH_SCHEME://kilometer.dev")
                append("response_type", "code")
                append("approval_prompt", "auto")
                append("scope", "activity:read_all")
            }
        }.buildString()

    fun linkUrl(id: Long) = "https://strava.com/activities/$id"
}
