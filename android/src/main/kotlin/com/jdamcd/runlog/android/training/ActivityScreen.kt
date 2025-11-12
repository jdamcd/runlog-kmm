package com.jdamcd.runlog.android.training

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.jdamcd.runlog.android.R
import com.jdamcd.runlog.android.main.AppBarState
import com.jdamcd.runlog.android.ui.ActivityIcons
import com.jdamcd.runlog.android.ui.LoadingScreen
import com.jdamcd.runlog.android.ui.RetryScreen
import com.jdamcd.runlog.android.ui.SplitBar
import com.jdamcd.runlog.android.ui.previewBackground
import com.jdamcd.runlog.android.ui.stravaBrand
import com.jdamcd.runlog.shared.ActivityDetails
import com.jdamcd.runlog.shared.ActivitySubtype
import com.jdamcd.runlog.shared.ActivityType
import com.jdamcd.runlog.shared.ImageUrl
import com.jdamcd.runlog.shared.Split
import kotlinx.coroutines.flow.StateFlow

@Composable
fun ActivityScreen(
    viewModel: ActivityViewModel,
    hostAppBar: (AppBarState) -> Unit,
    openLink: (String) -> Unit
) {
    var screenTitle by rememberSaveable { mutableStateOf("") }
    hostAppBar(
        AppBarState(title = screenTitle)
    )
    ActivityStates(
        stateFlow = viewModel.flow,
        onRetryClick = { viewModel.load() },
        onLinkClick = { openLink(viewModel.activityWebLink()) },
        updateTitle = { screenTitle = it }
    )
}

@Composable
private fun ActivityStates(
    stateFlow: StateFlow<ActivityState>,
    onRetryClick: () -> Unit,
    onLinkClick: () -> Unit,
    updateTitle: (String) -> Unit
) {
    Box {
        val state: ActivityState by stateFlow.collectAsState()
        when (state) {
            is ActivityState.Loading -> LoadingScreen()
            is ActivityState.Error -> RetryScreen(onRetryClick)
            is ActivityState.Data -> {
                val activity = (state as ActivityState.Data).activity
                updateTitle(activity.name)
                ActivityContent(activity, onLinkClick)
            }
        }
    }
}

@Preview(backgroundColor = previewBackground, showBackground = true)
@Composable
private fun ActivityContent(
    @PreviewParameter(ActivityContentProvider::class) activity: ActivityDetails,
    onClickLink: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top
    ) {
        activity.mapUrl?.let {
            AsyncImage(
                model = if (isSystemInDarkTheme()) it.darkMode else it.default,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2.5f),
                contentScale = ContentScale.FillWidth
            )
        }
        DescriptionHeader(activity)
        SummaryStats(activity)
        if (activity.splits.isNotEmpty()) {
            SplitsList(activity.splits)
        }
        WebLink(onClickLink)
    }
}

@Composable
private fun DescriptionHeader(activity: ActivityDetails) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.surface)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column {
            activity.description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.body1
                )
            }
            Text(
                text = activity.start,
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Light
            )
        }
        ActivityIcons(
            type = activity.type,
            subtype = activity.subtype
        )
    }
}

@Composable
private fun SummaryStats(activity: ActivityDetails) {
    if (activity.type == ActivityType.CROSS_TRAIN) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatBox(
                title = R.string.activity_stat_elapsed_time,
                stat = activity.elapsedDuration
            )
        }
    } else {
        RunRideSummaryStats(activity)
    }
}

@Composable
private fun RunRideSummaryStats(activity: ActivityDetails) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatBox(
            title = R.string.activity_stat_distance,
            stat = activity.distance
        )
        StatBox(
            title = R.string.activity_stat_pace,
            stat = activity.pace
        )
        if (activity.subtype.isRace()) {
            StatBox(
                title = R.string.activity_stat_elapsed_time,
                stat = activity.elapsedDuration
            )
        } else {
            StatBox(
                title = R.string.activity_stat_moving_time,
                stat = activity.movingDuration
            )
        }
    }
    if (hasHeartrateStats(activity)) {
        Divider(modifier = Modifier.padding(horizontal = 16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatBox(
                title = R.string.activity_stat_effort,
                stat = "${activity.effort}"
            )
            StatBox(
                title = R.string.activity_stat_hr_avg,
                stat = "${activity.averageHeartrate}"
            )
            StatBox(
                title = R.string.activity_stat_hr_max,
                stat = "${activity.maxHeartrate}"
            )
        }
    }
    Divider(modifier = Modifier.padding(horizontal = 16.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatBox(
            title = R.string.activity_stat_elevation,
            stat = activity.elevationGain
        )
        StatBox(
            title = R.string.activity_stat_calories,
            stat = "${activity.calories}"
        )
    }
}

@Composable
private fun StatBox(@StringRes title: Int, stat: String) {
    Column(
        modifier = Modifier
            .padding(12.dp)
            .width(100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.subtitle2
        )
        Text(
            text = stat,
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
private fun SplitsList(splits: List<Split>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Text(
                text = stringResource(R.string.activity_split_k),
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Start,
                modifier = Modifier.width(40.dp)
            )
            Text(
                text = stringResource(R.string.activity_split_pace),
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Start,
                modifier = Modifier.width(80.dp)
            )
        }
        Row {
            Text(
                text = stringResource(R.string.activity_split_elevation),
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.End,
                modifier = Modifier.width(80.dp)
            )
            Text(
                text = stringResource(R.string.activity_split_hr),
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.End,
                modifier = Modifier.width(60.dp)
            )
        }
    }
    Column(modifier = Modifier.padding(top = 6.dp)) {
        for (split in splits) {
            SplitItem(split)
        }
    }
}

@Composable
private fun SplitItem(split: Split) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            if (split.isPartial) {
                Text(
                    text = split.distance,
                    style = MaterialTheme.typography.body1,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.width(44.dp)
                )
            } else {
                Text(
                    text = "${split.number}",
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.width(44.dp)
                )
            }
            Text(
                text = split.pace,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Start,
                modifier = Modifier.width(60.dp)
            )
        }
        SplitBar(
            split.visualisation,
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        )
        Row {
            Text(
                text = split.elevation,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.End,
                modifier = Modifier.width(48.dp)
            )
            Text(
                text = split.averageHeartrate,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.End,
                modifier = Modifier.width(60.dp)
            )
        }
    }
}

@Composable
private fun WebLink(onClickLink: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            modifier = Modifier.padding(28.dp),
            onClick = onClickLink,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = stravaBrand
            )
        ) {
            Text(stringResource(R.string.strava_view))
        }
    }
}

private fun hasHeartrateStats(activity: ActivityDetails): Boolean = activity.effort != null &&
    activity.averageHeartrate != null &&
    activity.maxHeartrate != null

private class ActivityContentProvider : PreviewParameterProvider<ActivityDetails> {
    override val values = sequenceOf(
        ActivityDetails(
            id = 123,
            name = "Afternoon Run",
            description = "Easy Peckham Rye loop",
            type = ActivityType.RUN,
            subtype = ActivitySubtype.DEFAULT,
            kudos = 2,
            distance = "5k",
            elapsedDuration = "27:05",
            movingDuration = "24:39",
            elevationGain = "41m",
            elevationLow = "19m",
            elevationHigh = "47.8m",
            effort = 34,
            calories = 371,
            averageHeartrate = 151,
            maxHeartrate = 167,
            pace = "4:54 /km",
            start = "FRIDAY 25 NOV @ 2:23PM",
            mapUrl = ImageUrl(""),
            splits = listOf(
                Split(
                    number = 1,
                    distance = "1k",
                    isPartial = false,
                    elapsedDuration = "5:03",
                    movingDuration = "5:03",
                    elevation = "14",
                    averageHeartrate = "145",
                    pace = "5:03",
                    paceSeconds = 303,
                    paceZone = 2,
                    visualisation = 1.0f
                ),
                Split(
                    number = 2,
                    distance = "1k",
                    isPartial = false,
                    elapsedDuration = "5:04",
                    movingDuration = "5:04",
                    elevation = "12",
                    averageHeartrate = "159",
                    pace = "5:04",
                    paceSeconds = 304,
                    paceZone = 2,
                    visualisation = 1.0f
                ),
                Split(
                    number = 3,
                    distance = "1k",
                    isPartial = false,
                    elapsedDuration = "5:06",
                    movingDuration = "5:06",
                    elevation = "-15",
                    averageHeartrate = "158",
                    pace = "5:06",
                    paceSeconds = 306,
                    paceZone = 1,
                    visualisation = 0.9f
                ),
                Split(
                    number = 4,
                    distance = "1k",
                    isPartial = false,
                    elapsedDuration = "5:23",
                    movingDuration = "4:35",
                    elevation = "-6",
                    averageHeartrate = "150",
                    pace = "5:22",
                    paceSeconds = 322,
                    paceZone = 2,
                    visualisation = 0.8f
                ),
                Split(
                    number = 5,
                    distance = "0.5",
                    isPartial = true,
                    elapsedDuration = "6:16",
                    movingDuration = "4:41",
                    elevation = "-5",
                    averageHeartrate = "143",
                    pace = "6:16",
                    paceSeconds = 376,
                    paceZone = 2,
                    visualisation = 0.7f
                )
            )
        )
    )
}
