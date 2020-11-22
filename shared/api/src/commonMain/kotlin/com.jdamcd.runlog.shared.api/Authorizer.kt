package com.jdamcd.runlog.shared.api

import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.takeFrom
import io.ktor.client.statement.HttpReceivePipeline
import io.ktor.client.utils.EmptyContent
import io.ktor.http.HttpStatusCode
import io.ktor.util.AttributeKey

internal class Authorizer(
    private val tokenProvider: TokenProvider,
) {
    class Config {
        lateinit var tokenProvider: TokenProvider
    }

    companion object Feature : HttpClientFeature<Config, Authorizer> {

        override fun prepare(block: Config.() -> Unit): Authorizer {
            val config = Config().apply(block)
            return Authorizer(config.tokenProvider)
        }

        override fun install(feature: Authorizer, scope: HttpClient) {
            val state = feature.tokenProvider
            scope.requestPipeline.intercept(HttpRequestPipeline.State) {
                if (state.isLoggedIn()) {
                    context.headers[HEADER_AUTH] = "$TYPE_BEARER ${state.accessToken}"
                }
                proceed()
            }
            scope.receivePipeline.intercept(HttpReceivePipeline.After) {
                if (subject.status == HttpStatusCode.Unauthorized &&
                    !context.request.headers.contains(HEADER_REFRESH)
                ) {
                    try {
                        updateTokens(scope, state)

                        val builder = HttpRequestBuilder().takeFrom(context.request)
                        builder.header(HEADER_REFRESH, true.toString())
                        proceedWith((scope.requestPipeline.execute(builder, EmptyContent) as HttpClientCall).response)

                        return@intercept
                    } catch (throwable: Throwable) {} // Return 401
                }
                proceedWith(subject)
            }
        }

        override val key: AttributeKey<Authorizer> = AttributeKey("Authorizer")

        private suspend fun updateTokens(scope: HttpClient, state: TokenProvider) {
            val apiToken = refreshCall(scope, state.refreshToken)
            state.accessToken = apiToken.access_token
            state.refreshToken = apiToken.refresh_token
        }

        private suspend fun refreshCall(client: HttpClient, refreshToken: String): ApiToken {
            return client.post("${StravaApi.BASE_URL}/oauth/token") {
                parameter("refresh_token", refreshToken)
                parameter("client_id", BuildKonfig.CLIENT_ID)
                parameter("client_secret", BuildKonfig.CLIENT_SECRET)
                parameter("grant_type", "refresh_token")
            }
        }

        private const val HEADER_AUTH = "Authorization"
        private const val HEADER_REFRESH = "X-Token-Refresh"
        private const val TYPE_BEARER = "Bearer"
    }
}
