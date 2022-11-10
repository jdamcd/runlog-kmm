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
        val base = "https://api.mapbox.com/styles/v1/mapbox"
        return URLBuilder(base).apply {
            appendPathSegments(
                "light-v10",
                "static",
                "path($pathPolyline)",
                "auto",
                "${width}x$height@2x"
            )
            parameters.append("access_token", accessToken)
        }.buildString()
    }
}
