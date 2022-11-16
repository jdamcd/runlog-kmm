package com.jdamcd.runlog.android.training

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.DirectionsBike
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.SportsScore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.jdamcd.runlog.android.R
import com.jdamcd.runlog.android.ui.LoadingScreen
import com.jdamcd.runlog.android.ui.RetryScreen
import com.jdamcd.runlog.android.ui.stravaBrand
import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.ActivityType
import kotlinx.coroutines.flow.StateFlow

@Composable
fun TrainingScreen(
    viewModel: TrainingViewModel,
    onOpenLink: (String) -> Unit,
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
            TrainingList(
                stateFlow = viewModel.flow,
                modifier = Modifier.padding(padding),
                onItemClick = { onOpenLink(viewModel.generateLink(it)) },
                onRetryClick = { viewModel.load() }
            )
        }
    )
}

@Composable
private fun TrainingList(
    stateFlow: StateFlow<TrainingState>,
    modifier: Modifier,
    onItemClick: (Long) -> Unit,
    onRetryClick: () -> Unit
) {
    Box(modifier = modifier) {
        val state: TrainingState by stateFlow.collectAsState()
        when (state) {
            is TrainingState.Loading -> LoadingScreen()
            is TrainingState.Error -> RetryScreen(onRetryClick)
            is TrainingState.Data -> ActivityItems((state as TrainingState.Data).activityCards, onItemClick)
        }
    }
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
private fun ActivityItems(
    @PreviewParameter(ActivityItemsProvider::class) data: List<ActivityCard>,
    onItemClick: (Long) -> Unit = {}
) {
    LazyColumn {
        items(
            items = data,
            itemContent = { activity ->
                ActivityItem(activity = activity, onItemClick = onItemClick)
            }
        )
    }
}

@Composable
private fun ActivityItem(activity: ActivityCard, onItemClick: (Long) -> Unit) {
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
            val (stats, icons, link) = createRefs()

            ActivityStats(
                activity = activity,
                modifier = Modifier.constrainAs(stats) {
                    start.linkTo(parent.start)
                    end.linkTo(link.start)
                    width = Dimension.fillToConstraints
                }
            )
            ActivityIcons(
                activity = activity,
                modifier = Modifier.constrainAs(icons) {
                    top.linkTo(parent.top, margin = 8.dp)
                    end.linkTo(parent.end)
                }
            )
            Text(
                text = stringResource(R.string.strava_view),
                style = MaterialTheme.typography.body2,
                color = stravaBrand,
                modifier = Modifier.constrainAs(link) {
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
            )
        }

        activity.mapUrl?.let {
            AsyncImage(
                model = it,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 0.dp,
                        bottom = 8.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
                    .aspectRatio(2.5f)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.FillWidth
            )
        }
        Divider(modifier = Modifier.padding(horizontal = 16.dp))
    }
}

@Composable
private fun ActivityStats(activity: ActivityCard, modifier: Modifier) {
    Column(modifier = modifier) {
        Text(
            text = activity.name,
            style = MaterialTheme.typography.subtitle1
        )
        Row {
            Text(
                text = activity.distance,
                style = MaterialTheme.typography.h4,
                modifier = Modifier.alignByBaseline()
            )
            Text(
                text = "  ·  ${activity.duration}",
                style = MaterialTheme.typography.body2,
                modifier = Modifier.alignByBaseline()
            )
        }
        Text(
            text = activity.start,
            style = MaterialTheme.typography.overline
        )
    }
}

@Composable
private fun ActivityIcons(activity: ActivityCard, modifier: Modifier) {
    Row(modifier = modifier) {
        Icon(
            imageVector = when (activity.type) {
                ActivityType.RUN -> Icons.Rounded.DirectionsRun
                ActivityType.CYCLE -> Icons.Rounded.DirectionsBike
                ActivityType.CROSS_TRAIN -> Icons.Rounded.FitnessCenter
            },
            contentDescription = null,
            tint = MaterialTheme.colors.primaryVariant
        )
        if (activity.isRace) {
            Icon(
                imageVector = Icons.Rounded.SportsScore,
                contentDescription = null,
                tint = stravaBrand
            )
        }
    }
}

private class ActivityItemsProvider : PreviewParameterProvider<List<ActivityCard>> {
    override val values = sequenceOf(
        listOf(
            ActivityCard(
                id = 1,
                name = "NYC Marathon",
                type = ActivityType.RUN,
                isRace = true,
                distance = "42.2k",
                duration = "2:59:59",
                start = "SUNDAY 6 NOV @ 9:11AM",
                mapUrl = "example.com"
            ),
            ActivityCard(
                id = 2,
                name = "Morning Run",
                type = ActivityType.RUN,
                isRace = false,
                distance = "12.3k",
                duration = "1:02:17",
                start = "SATURDAY 12 NOV @ 8:37AM",
                mapUrl = null
            )
        )
    )
}
