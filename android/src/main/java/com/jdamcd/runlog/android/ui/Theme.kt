package com.jdamcd.runlog.android.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun RunLogTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = if (darkTheme) runLogDarkColors() else runLogLightColors(),
        typography = runLogTypography(),
        content = content
    )
}

private fun runLogLightColors() = lightColors(
    primary = themePrimary,
    primaryVariant = themeDark,
    secondary = themeAccent,
    surface = surface
)

private fun runLogDarkColors() = darkColors(
    primary = themeDark,
    primaryVariant = themeAccent,
    secondary = themePrimary,
    surface = surfaceDark,
    background = black,
    onPrimary = white,
)

private fun runLogTypography() = Typography(
    subtitle2 = TextStyle(
        fontWeight = FontWeight.Light,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp,
        color = textSubtitle
    )
)