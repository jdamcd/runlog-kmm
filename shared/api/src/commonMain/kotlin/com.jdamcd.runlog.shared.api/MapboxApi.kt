package com.jdamcd.runlog.shared.api

import io.ktor.http.URLBuilder
import io.ktor.http.appendPathSegments

object MapboxApi {

    fun staticMap(
        pathPolyline: String,
        width: Int = 400,
        height: Int = 160,
        accessToken: String = BuildKonfig.MAPBOX_TOKEN
    ): String {
        return URLBuilder("https://api.mapbox.com/styles/v1/mapbox/light-v10/static").apply {
            appendPathSegments("path($pathPolyline)", "auto", "${width}x$height@2x")
            parameters.apply {
                append("access_token", accessToken)
            }
        }.buildString()
    }
}
