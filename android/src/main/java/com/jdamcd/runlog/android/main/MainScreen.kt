package com.jdamcd.runlog.android.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
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
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.jdamcd.runlog.android.R
import com.jdamcd.runlog.android.ui.RunLogTheme
import com.jdamcd.runlog.android.ui.stravaBrand
import com.jdamcd.runlog.shared.ActivityCard

@Composable
fun ActivityFeed(
    liveData: LiveData<ActivityFeedState>,
    onItemClick: (Long) -> Unit,
    onRetryClick: () -> Unit,
    onSignOutClick: () -> Unit
) {
    RunLogTheme {
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
                    navigationIcon = {
                        Image(
                            asset = vectorResource(id = R.drawable.vector_logo),
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.primaryVariant),
                            modifier = Modifier.padding(
                                top = 8.dp,
                                bottom = 8.dp,
                                start = 8.dp,
                                end = 0.dp
                            )
                        )
                    },
                    backgroundColor = MaterialTheme.colors.background,
                    actions = {
                        IconButton(onClick = onSignOutClick) {
                            Icon(
                                asset = Icons.Outlined.ExitToApp,
                                tint = MaterialTheme.colors.primaryVariant
                            )
                        }
                    }
                )
            },
            bodyContent = { MainContent(liveData, onItemClick, onRetryClick) }
        )
    }
}

@Composable
private fun MainContent(
    liveData: LiveData<ActivityFeedState>,
    onItemClick: (Long) -> Unit,
    onRetryClick: () -> Unit
) {
    val state: ActivityFeedState by liveData.observeAsState(initial = ActivityFeedState.Loading)
    when (state) {
        is ActivityFeedState.Loading -> LoadingScreen()
        is ActivityFeedState.Error -> RetryScreen(onRetryClick)
        is ActivityFeedState.Data -> ActivitiesList((state as ActivityFeedState.Data), onItemClick)
    }
}

@Composable
private fun ActivitiesList(data: ActivityFeedState.Data, onItemClick: (Long) -> Unit) {
    LazyColumnFor(items = data.activityCards) { activity ->
        ActivityListItem(activity, onItemClick)
    }
}

@Composable
private fun ActivityListItem(activity: ActivityCard, onItemClick: (Long) -> Unit) {
    Column(
        modifier = Modifier
            .clickable(onClick = { onItemClick(activity.id) })
            .fillMaxWidth()
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(vertical = 8.dp, horizontal = 16.dp)
                .fillMaxWidth()
        ) {
            val (mainColumn, link) = createRefs()
            Column(
                Modifier.constrainAs(mainColumn) {
                    start.linkTo(parent.start)
                }
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
                    text = activity.time,
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text = activity.distance,
                    style = MaterialTheme.typography.body2
                )
            }
            Text(
                text = stringResource(R.string.strava_view),
                style = MaterialTheme.typography.body2,
                color = stravaBrand,
                modifier = Modifier.constrainAs(link) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
            )
        }
        Divider(modifier = Modifier.padding(horizontal = 16.dp))
    }
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
