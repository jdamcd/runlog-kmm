package com.jdamcd.runlog.android.login

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.jdamcd.runlog.android.R
import com.jdamcd.runlog.android.ui.RunLogTheme

@Composable
fun Login(liveData: LiveData<LoginState>, onConnectClick: () -> Unit) {
    RunLogTheme {
        Box {
            GradientBackground()
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                val (logo, connectButton, attribution) = createRefs()
                Image(
                    asset = vectorResource(id = R.drawable.vector_logo),
                    modifier = Modifier
                        .width(200.dp)
                        .height(200.dp)
                        .constrainAs(logo) {
                            top.linkTo(parent.top)
                            bottom.linkTo(connectButton.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )
                ConnectLoadable(
                    liveData = liveData,
                    connectClick = onConnectClick,
                    modifier = Modifier.constrainAs(connectButton) {
                        top.linkTo(logo.bottom)
                        bottom.linkTo(attribution.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )
                Image(
                    asset = vectorResource(id = R.drawable.vector_strava_powered),
                    modifier = Modifier.constrainAs(attribution) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )
            }
        }
    }
}

@Composable
private fun ConnectLoadable(
    liveData: LiveData<LoginState>,
    connectClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state: LoginState by liveData.observeAsState(initial = LoginState.Idle)
    Box(modifier = modifier.height(50.dp)) {
        when (state) {
            is LoginState.Idle -> {
                Button(
                    contentPadding = PaddingValues(0.dp),
                    backgroundColor = Color.Transparent,
                    elevation = 0.dp,
                    onClick = connectClick
                ) {
                    Image(asset = vectorResource(id = R.drawable.vector_strava_connect))
                }
            }
            is LoginState.Loading, LoginState.Success -> {
                CircularProgressIndicator(color = MaterialTheme.colors.background)
            }
        }
    }
}

@Composable
fun GradientBackground() {
    val start = MaterialTheme.colors.primary
    val end = MaterialTheme.colors.primaryVariant
    Canvas(
        modifier = Modifier.fillMaxSize(),
        onDraw = {
            drawRect(
                LinearGradient(
                    colors = listOf(start, end),
                    startX = 0f,
                    startY = 0f,
                    endX = 0f,
                    endY = size.height
                )
            )
        }
    )
}
