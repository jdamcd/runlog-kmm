package com.jdamcd.runlog.android.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.jdamcd.runlog.android.ui.divider
import com.jdamcd.runlog.android.ui.stravaBrand
import com.jdamcd.runlog.shared.AthleteProfile
import com.jdamcd.runlog.shared.AthleteStats
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

@Preview(backgroundColor = 0xffffffff, showBackground = true)
@Composable
private fun ProfileContent(
    @PreviewParameter(ProfileContentProvider::class) profile: AthleteProfile
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp, start = 8.dp, end = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
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
            text = profile.username,
            style = MaterialTheme.typography.body1
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProfileStat(
                title = stringResource(R.string.profile_stat_30d),
                primary = profile.recentRuns.distance,
                secondary = profile.recentRuns.pace
            )
            VerticalDivider()
            ProfileStat(
                title = stringResource(R.string.profile_stat_year_to_date),
                primary = profile.yearRuns.distance,
                secondary = profile.yearRuns.pace
            )
            VerticalDivider()
            ProfileStat(
                title = stringResource(R.string.profile_stat_all_time),
                primary = profile.allRuns.distance,
                secondary = profile.allRuns.pace
            )
        }
    }
}

@Composable
private fun ProfileStat(
    title: String,
    primary: String,
    secondary: String
) {
    Column(
        modifier = Modifier.width(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.Bold,
            color = stravaBrand,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = primary,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.body1
        )
        Text(
            text = secondary,
            style = MaterialTheme.typography.body2
        )
    }
}

@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(60.dp)
            .background(divider)
    )
}

private class ProfileContentProvider : PreviewParameterProvider<AthleteProfile> {
    override val values = sequenceOf(
        AthleteProfile(
            id = 1,
            username = "jdamcd",
            name = "Jamie McDonald",
            imageUrl = "example.com",
            recentRuns = AthleteStats(
                distance = "123.4k",
                pace = "4:50/k"
            ),
            yearRuns = AthleteStats(
                distance = "1,234k",
                pace = "5:01/k"
            ),
            allRuns = AthleteStats(
                distance = "12,345k",
                pace = "5:25/k"
            )
        )
    )
}
