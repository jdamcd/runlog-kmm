package com.jdamcd.runlog.shared.api

import io.ktor.http.URLBuilder
import io.ktor.http.appendPathSegments

object MapboxStatic {

    fun makeUrl(
        pathPolyline: String,
        width: Int = 400,
        height: Int = 160,
        darkMode: Boolean = false,
        accessToken: String = BuildKonfig.MAPBOX_TOKEN
    ): String {
        val base = "https://api.mapbox.com/styles/v1/mapbox"
        val theme = if (darkMode) "dark-v10" else "light-v10"
        val pathStyle = if (darkMode) "path+fff" else "path"
        return URLBuilder(base).apply {
            appendPathSegments(
                theme,
                "static",
                "$pathStyle($pathPolyline)",
                "auto",
                "${width}x$height@2x"
            )
            parameters.append("logo", "false")
            parameters.append("access_token", accessToken)
        }.buildString()
    }
}
