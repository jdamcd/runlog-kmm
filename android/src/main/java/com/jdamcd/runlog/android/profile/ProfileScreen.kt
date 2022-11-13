package com.jdamcd.runlog.android.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
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
                stateFlow = viewModel.flow,
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

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
private fun ProfileContent(
    @PreviewParameter(ProfileContentProvider::class) profile: AthleteProfile
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = profile.imageUrl,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            contentDescription = null
        )
        Text(
            text = profile.name,
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = profile.yearRunDistance,
            style = MaterialTheme.typography.body1
        )
    }
}

private class ProfileContentProvider : PreviewParameterProvider<AthleteProfile> {
    override val values = sequenceOf(
        AthleteProfile(
            id = 1,
            username = "jdamcd",
            name = "Jamie McDonald",
            imageUrl = "example.com",
            yearRunDistance = "1,234km",
            allTimeRunDistance = "12,345km"
        )
    )
}
