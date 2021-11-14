package com.jdamcd.runlog.shared.api

import co.touchlab.stately.concurrency.AtomicReference
import co.touchlab.stately.concurrency.value
import co.touchlab.stately.ensureNeverFrozen
import com.jdamcd.runlog.shared.util.Logger
import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLBuilder
import kotlinx.serialization.json.Json

class StravaApi(tokenProvider: TokenProvider) {

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
            this.tokenProvider = tokenProvider
        }
    }

    private val atom = AtomicReference(tokenProvider)

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
        return try {
            client.get("$BASE_URL/athlete/activities")
        } catch (error: Throwable) {
            throw if (isAuthError(error)) {
                Logger.debug("Unhandled 401 fetching activities")
                AuthException("Unhandled 401", error)
            } else error
        }
    }

    private fun isAuthError(error: Throwable): Boolean {
        return error is ClientRequestException &&
            error.response.status == HttpStatusCode.Unauthorized
    }

    companion object {
        const val BASE_URL = "https://www.strava.com/api/v3"
        const val AUTH_SCHEME = "runlog-auth"

        fun loginUrl(clientId: String = BuildKonfig.CLIENT_ID) =
            URLBuilder("https://strava.com/oauth/mobile/authorize").apply {
                parameters.apply {
                    append("client_id", clientId)
                    append("redirect_uri", "$AUTH_SCHEME://jdamcd.com")
                    append("response_type", "code")
                    append("approval_prompt", "auto")
                    append("scope", "activity:read")
                }
            }.buildString()

        fun linkUrl(id: Long) = "https://strava.com/activities/$id"
    }
}

class AuthException(
    override val message: String,
    override val cause: Throwable
) : Throwable()
