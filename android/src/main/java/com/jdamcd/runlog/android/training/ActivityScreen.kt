package com.jdamcd.runlog.android.training

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.jdamcd.runlog.android.R
import com.jdamcd.runlog.android.ui.stravaBrand

@Composable
fun ActivityScreen(
    id: Long,
    viewModel: ActivityViewModel,
    onOpenLink: (String) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { onOpenLink(viewModel.generateLink(id)) },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = stravaBrand,
                contentColor = Color.White
            )
        ) {
            Text(stringResource(R.string.strava_view))
        }
    }
}
