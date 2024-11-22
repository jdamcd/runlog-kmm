package com.jdamcd.runlog.shared.api

import com.jdamcd.runlog.shared.util.DebugConfig
import com.jdamcd.runlog.shared.util.Log
import com.jdamcd.runlog.shared.util.MultiLog
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
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
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

interface StravaApi {
    suspend fun tokenRefresh(refreshToken: String): ApiToken
    suspend fun tokenExchange(code: String): ApiDetailedAthlete
    suspend fun activities(pageSize: Int = 100, page: Int = 1): List<ApiSummaryActivity>
    suspend fun activity(id: Long): ApiDetailedActivity
    suspend fun athlete(): ApiDetailedAthlete
    suspend fun athleteStats(id: Long): ApiActivityStats
}

@OptIn(ExperimentalSerializationApi::class)
internal class KtorStravaApi(
    private val tokenProvider: TokenProvider,
    private val log: Log = MultiLog()
) : StravaApi {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                    explicitNulls = false
                }
            )
        }
        install(Auth) {
            bearer {
                loadTokens {
                    BearerTokens(tokenProvider.accessToken, tokenProvider.refreshToken)
                }
                refreshTokens {
                    val apiToken = tokenRefresh(tokenProvider.refreshToken)
                    tokenProvider.store(apiToken.access_token, apiToken.refresh_token)
                    BearerTokens(apiToken.access_token, apiToken.refresh_token)
                }
            }
        }
        HttpResponseValidator {
            handleResponseExceptionWithRequest { exception, _ ->
                log.error(exception.stackTraceToString())
                val clientException = exception as? ClientRequestException ?: return@handleResponseExceptionWithRequest
                if (clientException.response.status == HttpStatusCode.Unauthorized) {
                    log.debug("Unhandled 401")
                    throw AuthException("Unhandled 401", clientException)
                }
            }
        }
        if (DebugConfig.isDebug) {
            install(Logging) {
                level = LogLevel.INFO
                logger = object : Logger {
                    override fun log(message: String) {
                        log.debug(message)
                    }
                }
            }
        }
    }

    override suspend fun tokenRefresh(refreshToken: String): ApiToken = client.post("$BASE_URL/oauth/token") {
        parameter("refresh_token", refreshToken)
        parameter("client_id", BuildKonfig.CLIENT_ID)
        parameter("client_secret", BuildKonfig.CLIENT_SECRET)
        parameter("grant_type", "refresh_token")
    }.body()

    override suspend fun tokenExchange(code: String): ApiDetailedAthlete {
        val result = client.post("$BASE_URL/oauth/token") {
            parameter("code", code)
            parameter("client_id", BuildKonfig.CLIENT_ID)
            parameter("client_secret", BuildKonfig.CLIENT_SECRET)
            parameter("grant_type", "authorization_code")
        }.body<ApiTokenWithAthlete>()
        tokenProvider.store(result.access_token, result.refresh_token)
        return result.athlete
    }

    override suspend fun activities(pageSize: Int, page: Int): List<ApiSummaryActivity> = client.get("$BASE_URL/athlete/activities") {
        parameter("per_page", pageSize)
        parameter("page", page)
    }.body()

    override suspend fun activity(id: Long): ApiDetailedActivity = client.get("$BASE_URL/activities/$id").body()

    override suspend fun athlete(): ApiDetailedAthlete = client.get("$BASE_URL/athlete").body()

    override suspend fun athleteStats(id: Long): ApiActivityStats = client.get("$BASE_URL/athletes/$id/stats").body()
}

internal const val BASE_URL = "https://www.strava.com/api/v3"

class AuthException(
    override val message: String,
    override val cause: Throwable
) : Throwable()
