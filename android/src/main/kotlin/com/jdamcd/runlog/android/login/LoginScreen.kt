package com.jdamcd.runlog.android.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jdamcd.runlog.android.R
import com.jdamcd.runlog.android.ui.RunLogTheme
import com.jdamcd.runlog.android.ui.white
import kotlinx.coroutines.flow.StateFlow

@Composable
fun Login(
    viewModel: LoginViewModel,
    onOpenLink: (String) -> Unit
) {
    RunLogTheme {
        Box {
            LoginBackground()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = 16.dp,
                        top = 160.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    ),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.vector_logo),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .width(300.dp)
                        .height(300.dp)
                )
                ConnectLoadable(
                    stateFlow = viewModel.flow,
                    onConnectClick = {
                        val url = viewModel.startLogin()
                        onOpenLink(url)
                    }
                )
                Image(
                    painter = painterResource(id = R.drawable.vector_strava_powered),
                    contentDescription = stringResource(id = R.string.strava_view)
                )
            }
        }
    }
}

@Composable
private fun ConnectLoadable(
    stateFlow: StateFlow<LoginState>,
    onConnectClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state: LoginState by stateFlow.collectAsState()
    Box(modifier = modifier.height(50.dp)) {
        when (state) {
            is LoginState.Idle -> {
                Row(
                    Modifier.clickable(onClick = onConnectClick)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.vector_strava_connect),
                        contentDescription = stringResource(id = R.string.strava_connect)
                    )
                }
            }
            is LoginState.Loading, LoginState.Success -> {
                CircularProgressIndicator(color = white)
            }
        }
    }
}

@Preview
@Composable
private fun LoginBackground() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painterResource(id = R.drawable.login_background),
                contentScale = ContentScale.FillBounds
            )
    )
}
