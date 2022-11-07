package com.jdamcd.runlog.android.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.jdamcd.runlog.android.R
import com.jdamcd.runlog.android.ui.LoadingScreen
import com.jdamcd.runlog.android.ui.RetryScreen
import com.jdamcd.runlog.shared.AthleteProfile
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onSignOutClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.profile_title),
                        color = MaterialTheme.colors.primaryVariant,
                        fontWeight = FontWeight.Bold
                    )
                },
                backgroundColor = MaterialTheme.colors.background,
                actions = {
                    IconButton(onClick = onSignOutClick) {
                        Icon(
                            imageVector = Icons.Rounded.ExitToApp,
                            contentDescription = stringResource(R.string.sign_out),
                            tint = MaterialTheme.colors.primaryVariant
                        )
                    }
                }
            )
        },
        content = { padding ->
            ProfileStates(
                stateFlow = viewModel.flow.also { viewModel.load() },
                modifier = Modifier.padding(padding),
                onRetryClick = { viewModel.load() }
            )
        }
    )
}

@Composable
private fun ProfileStates(
    stateFlow: StateFlow<ProfileState>,
    modifier: Modifier,
    onRetryClick: () -> Unit
) {
    Box(modifier = modifier) {
        val state: ProfileState by stateFlow.collectAsState()
        when (state) {
            is ProfileState.Loading -> LoadingScreen()
            is ProfileState.Error -> RetryScreen(onRetryClick)
            is ProfileState.Data -> ProfileContent((state as ProfileState.Data).profile)
        }
    }
}

@Composable
private fun ProfileContent(profile: AthleteProfile) {
    Column {
        Text(
            text = profile.name,
            style = MaterialTheme.typography.body2
        )
        Text(
            text = profile.yearRunDistance,
            style = MaterialTheme.typography.body2
        )
    }
}
