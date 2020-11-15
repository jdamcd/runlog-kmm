package com.jdamcd.runlog.shared.internal

import co.touchlab.stately.concurrency.AtomicReference
import co.touchlab.stately.concurrency.value
import co.touchlab.stately.ensureNeverFrozen
import com.jdamcd.runlog.shared.UserState
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.http.URLBuilder
import kotlinx.serialization.json.Json

internal class StravaApi(state: UserState) {

    private val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(
                Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
        install(Authorizer) {
            this.userState = state
        }
    }

    private val atom = AtomicReference(state)

    init {
        ensureNeverFrozen()
    }

    suspend fun tokenExchange(code: String) {
        val tokens = client.post<ApiToken>("$BASE_URL/oauth/token") {
            parameter("code", code)
            parameter("client_id", BuildKonfig.CLIENT_ID)
            parameter("client_secret", BuildKonfig.CLIENT_SECRET)
            parameter("grant_type", "authorization_code")
        }
        atom.value.accessToken = tokens.access_token
        atom.value.refreshToken = tokens.refresh_token
    }

    suspend fun activities(): List<ApiSummaryActivity> {
        return client.get("$BASE_URL/athlete/activities")
    }

    companion object {
        const val BASE_URL = "https://www.strava.com/api/v3"
        const val AUTH_REDIRECT = "runlog-auth://jdamcd.com"

        fun loginUrl(clientId: String = BuildKonfig.CLIENT_ID) =
            URLBuilder("https://strava.com/oauth/mobile/authorize").apply {
                parameters.apply {
                    append("client_id", clientId)
                    append("redirect_uri", AUTH_REDIRECT)
                    append("response_type", "code")
                    append("approval_prompt", "auto")
                    append("scope", "activity:read")
                }
            }.buildString()

        fun linkUrl(id: Long) = "https://strava.com/activities/$id"
    }
}
