package com.jdamcd.runlog.android.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DirectionsBike
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.SportsScore
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jdamcd.runlog.android.R
import com.jdamcd.runlog.shared.ActivitySubtype
import com.jdamcd.runlog.shared.ActivityType

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun RetryScreen(onRetryClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = onRetryClick,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(text = stringResource(R.string.retry_button))
        }
    }
}

@Composable
fun SplitBar(
    value: Float,
    modifier: Modifier = Modifier,
    color: Color = themePrimary
) {
    Canvas(
        modifier
            .sizeIn(minWidth = 100.dp, minHeight = 10.dp)
            .alpha(value)
    ) {
        drawRoundRect(
            color = color,
            size = Size(value * size.width, size.height),
            cornerRadius = CornerRadius(4.dp.toPx())
        )
    }
}

@Composable
fun ActivityIcons(
    type: ActivityType,
    subtype: ActivitySubtype,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Icon(
            imageVector = when (type) {
                ActivityType.RUN -> Icons.Rounded.DirectionsRun
                ActivityType.CYCLE -> Icons.Rounded.DirectionsBike
                ActivityType.CROSS_TRAIN -> Icons.Rounded.FitnessCenter
            },
            contentDescription = null,
            tint = themeDark
        )
        if (subtype.isRace()) {
            Icon(
                imageVector = Icons.Rounded.SportsScore,
                contentDescription = null,
                tint = themePrimary
            )
        }
    }
}
