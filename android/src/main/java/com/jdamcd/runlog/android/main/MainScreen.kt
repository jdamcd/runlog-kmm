package com.jdamcd.runlog.android.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jdamcd.runlog.android.R
import com.jdamcd.runlog.android.ui.RunLogTheme
import com.jdamcd.runlog.android.ui.stravaBrand
import com.jdamcd.runlog.shared.ActivityCard

const val NAV_ACTIVITIES = "activities"
const val NAV_PROFILE = "profile"

@Composable
fun CoreNavigation(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NAV_ACTIVITIES,
    onActivityClick: (Long) -> Unit,
    onSignOutClick: () -> Unit
) {
    RunLogTheme {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = startDestination
        ) {
            composable(NAV_ACTIVITIES) {
                ActivitiesScreen(
                    liveData = viewModel.uiModel,
                    onItemClick = { onActivityClick(it) },
                    onRetryClick = { viewModel.load() },
                    onNavigateToProfile = { navController.navigate(NAV_PROFILE) }
                )
            }
            composable(NAV_PROFILE) {
                ProfileScreen(
                    onSignOutClick = onSignOutClick
                )
            }
        }
    }
}

@Composable
fun ActivitiesScreen(
    liveData: LiveData<ActivityFeedState>,
    onItemClick: (Long) -> Unit,
    onRetryClick: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.activities_title),
                        color = MaterialTheme.colors.primaryVariant,
                        fontWeight = FontWeight.Bold
                    )
                },
                backgroundColor = MaterialTheme.colors.background,
                actions = {
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(
                            imageVector = Icons.Rounded.AccountCircle,
                            contentDescription = stringResource(R.string.profile_title),
                            tint = MaterialTheme.colors.primaryVariant
                        )
                    }
                }
            )
        },
        content = { padding ->
            ActivitiesList(liveData, Modifier.padding(padding), onItemClick, onRetryClick)
        }
    )
}

@Composable
private fun ActivitiesList(
    liveData: LiveData<ActivityFeedState>,
    modifier: Modifier,
    onItemClick: (Long) -> Unit,
    onRetryClick: () -> Unit
) {
    Box(modifier = modifier) {
        val state: ActivityFeedState by liveData.observeAsState(initial = ActivityFeedState.Loading)
        when (state) {
            is ActivityFeedState.Loading -> LoadingScreen()
            is ActivityFeedState.Error -> RetryScreen(onRetryClick)
            is ActivityFeedState.Data -> ActivityItems((state as ActivityFeedState.Data), onItemClick)
        }
    }
}

@Composable
private fun ActivityItems(data: ActivityFeedState.Data, onItemClick: (Long) -> Unit) {
    LazyColumn {
        items(
            items = data.activityCards,
            itemContent = { activity ->
                ActivityListItem(activity = activity, onItemClick = onItemClick)
            }
        )
    }
}

@Composable
private fun ActivityListItem(activity: ActivityCard, onItemClick: (Long) -> Unit) {
    Column(
        modifier = Modifier
            .clickable(onClick = { onItemClick(activity.id) })
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text(
                    text = "${activity.type}: ${activity.name}",
                    style = MaterialTheme.typography.subtitle2
                )
                activity.label?.let {
                    Text(
                        text = it,
                        color = stravaBrand,
                        style = MaterialTheme.typography.body2
                    )
                }
                Text(
                    text = activity.duration,
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text = activity.distance,
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text = activity.start,
                    style = MaterialTheme.typography.body2
                )
            }
            Text(
                text = stringResource(R.string.strava_view),
                style = MaterialTheme.typography.body2,
                color = stravaBrand
            )
        }
        Divider(modifier = Modifier.padding(horizontal = 16.dp))
    }
}

@Composable
fun ProfileScreen(
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
            Box(modifier = Modifier.padding(padding)) {
                // TODO
                LoadingScreen()
            }
        }
    )
}

@Composable
private fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun RetryScreen(onRetryClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Button(
            onClick = onRetryClick,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(text = stringResource(R.string.retry_button))
        }
    }
}
