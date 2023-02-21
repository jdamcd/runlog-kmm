package com.jdamcd.runlog.android.training

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import coil.compose.AsyncImage
import com.jdamcd.runlog.android.R
import com.jdamcd.runlog.android.main.AppBarState
import com.jdamcd.runlog.android.ui.ActivityIcons
import com.jdamcd.runlog.android.ui.LoadingScreen
import com.jdamcd.runlog.android.ui.RetryScreen
import com.jdamcd.runlog.android.ui.previewBackground
import com.jdamcd.runlog.shared.ActivityCard
import com.jdamcd.runlog.shared.ActivitySubtype
import com.jdamcd.runlog.shared.ActivityType
import com.jdamcd.runlog.shared.ImageUrl
import kotlinx.coroutines.flow.StateFlow

@Composable
fun TrainingScreen(
    viewModel: TrainingViewModel,
    hostAppBar: (AppBarState) -> Unit,
    navigateToActivity: (Long) -> Unit,
    navigateToProfile: () -> Unit
) {
    val toolbarState: ToolbarState by viewModel.toolbarFlow.collectAsState()
    hostAppBar(
        AppBarState(
            title = stringResource(R.string.activities_title),
            actions = {
                IconButton(onClick = navigateToProfile) {
                    when (toolbarState) {
                        ToolbarState.NoProfileImage -> {
                            Icon(
                                imageVector = Icons.Rounded.AccountCircle,
                                contentDescription = stringResource(R.string.profile_title),
                                tint = MaterialTheme.colors.onPrimary
                            )
                        }
                        is ToolbarState.ProfileImage -> {
                            AsyncImage(
                                model = (toolbarState as ToolbarState.ProfileImage).url,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .border(
                                        width = 1.dp,
                                        shape = CircleShape,
                                        color = MaterialTheme.colors.onPrimary
                                    )
                            )
                        }
                    }
                }
            }
        )
    )
    TrainingList(
        stateFlow = viewModel.contentFlow,
        onItemClick = { navigateToActivity(it) },
        onRetryClick = { viewModel.refresh() },
        onPullRefresh = { viewModel.refresh() }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun TrainingList(
    stateFlow: StateFlow<TrainingState>,
    onItemClick: (Long) -> Unit,
    onRetryClick: () -> Unit,
    onPullRefresh: () -> Unit
) {
    val state: TrainingState by stateFlow.collectAsState(TrainingState.Loading)
    val refreshState = rememberPullRefreshState(state.isRefreshing(), onPullRefresh)
    Box(modifier = Modifier.pullRefresh(refreshState)) {
        when (state) {
            is TrainingState.Loading -> LoadingScreen()
            is TrainingState.Data -> ActivityItems((state as TrainingState.Data).activityCards, onItemClick)
            is TrainingState.Refreshing -> ActivityItems((state as TrainingState.Refreshing).activityCards, onItemClick)
            is TrainingState.Error -> RetryScreen(onRetryClick)
        }
        PullRefreshIndicator(
            refreshing = state.isRefreshing(),
            state = refreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = MaterialTheme.colors.primaryVariant
        )
    }
}

@Preview(backgroundColor = previewBackground, showBackground = true)
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
            val (icons) = createRefs()
            ActivitySummary(activity)
            ActivityIcons(
                type = activity.type,
                subtype = activity.subtype,
                modifier = Modifier.constrainAs(icons) {
                    top.linkTo(parent.top, margin = 8.dp)
                    end.linkTo(parent.end)
                }
            )
        }

        activity.mapUrl?.let {
            AsyncImage(
                model = if (isSystemInDarkTheme()) it.darkMode else it.default,
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
private fun ActivitySummary(activity: ActivityCard) {
    if (activity.type == ActivityType.CROSS_TRAIN) {
        MiniSummary(activity)
    } else {
        FullSummary(activity)
    }
}

@Composable
private fun MiniSummary(activity: ActivityCard) {
    Column {
        Text(
            text = activity.name,
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = activity.duration,
            style = MaterialTheme.typography.h4
        )
        Text(
            text = activity.start,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
private fun FullSummary(activity: ActivityCard) {
    Column {
        Text(
            text = activity.name,
            style = MaterialTheme.typography.subtitle1,
            fontWeight = FontWeight.SemiBold
        )
        Row {
            Text(
                text = activity.distance,
                style = MaterialTheme.typography.h4,
                modifier = Modifier.alignByBaseline()
            )
            Text(
                text = "  ·  ${activity.duration}",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.alignByBaseline()
            )
            Text(
                text = "  ·  ${activity.pace}",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.alignByBaseline()
            )
        }
        Text(
            text = activity.start,
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.Light
        )
    }
}

private class ActivityItemsProvider : PreviewParameterProvider<List<ActivityCard>> {
    override val values = sequenceOf(
        listOf(
            ActivityCard(
                id = 1,
                name = "NYC Marathon",
                type = ActivityType.RUN,
                subtype = ActivitySubtype.RACE,
                distance = "42.2k",
                duration = "2:59:59",
                pace = "4:16 /km",
                start = "SUNDAY 6 NOV @ 9:11AM",
                mapUrl = ImageUrl("")
            ),
            ActivityCard(
                id = 2,
                name = "Yoga",
                type = ActivityType.CROSS_TRAIN,
                subtype = ActivitySubtype.DEFAULT,
                distance = "0k",
                duration = "30:00",
                pace = "0:00 /km",
                start = "SATURDAY 12 NOV @ 8:37AM",
                mapUrl = null
            )
        )
    )
}
