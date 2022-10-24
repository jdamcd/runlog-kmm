package com.jdamcd.runlog.shared.api

import com.jdamcd.runlog.shared.util.DebugConfig
import com.jdamcd.runlog.shared.util.MultiLog
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLBuilder
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class StravaApi(private val tokenProvider: TokenProvider) {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(tokenProvider.accessToken, tokenProvider.refreshToken)
                }
                refreshTokens {
                    val apiToken = refreshCall(tokenProvider.refreshToken)
                    tokenProvider.store(apiToken.access_token, apiToken.refresh_token)
                    BearerTokens(apiToken.access_token, apiToken.refresh_token)
                }
            }
        }
        if (DebugConfig.isDebug) {
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        MultiLog.debug(message)
                    }
                }
            }
        }
    }

    private suspend fun refreshCall(refreshToken: String): ApiToken {
        return client.post("$BASE_URL/oauth/token") {
            parameter("refresh_token", refreshToken)
            parameter("client_id", BuildKonfig.CLIENT_ID)
            parameter("client_secret", BuildKonfig.CLIENT_SECRET)
            parameter("grant_type", "refresh_token")
        }.body()
    }

    suspend fun tokenExchange(code: String) {
        client.post("$BASE_URL/oauth/token") {
            parameter("code", code)
            parameter("client_id", BuildKonfig.CLIENT_ID)
            parameter("client_secret", BuildKonfig.CLIENT_SECRET)
            parameter("grant_type", "authorization_code")
        }.body<ApiToken>().let { tokenProvider.store(it.access_token, it.refresh_token) }
    }

    suspend fun activities(): List<ApiSummaryActivity> {
        return try {
            client.get("$BASE_URL/athlete/activities").body()
        } catch (error: Throwable) {
            throw if (isAuthError(error)) {
                MultiLog.debug("Unhandled 401 fetching activities")
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
