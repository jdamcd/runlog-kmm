package com.jdamcd.runlog.android.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

@Composable
fun RunLogTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = lightColors(
            primary = themePrimary,
            primaryVariant = themeDark,
            secondary = themeAccent
        ),
        content = content
    )
}
