package com.jdamcd.runlog.android.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jdamcd.runlog.android.profile.ProfileScreen
import com.jdamcd.runlog.android.training.ActivityScreen
import com.jdamcd.runlog.android.training.TrainingScreen
import com.jdamcd.runlog.android.ui.RunLogTheme

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUTE_HOME,
    openLink: (String) -> Unit,
    signOut: () -> Unit
) {
    RunLogTheme {
        var appBarState by remember { mutableStateOf(AppBarState()) }
        Column(modifier = Modifier.fillMaxSize()) {
            // TopAppBar background extends behind the status bar
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.primary)
                    .statusBarsPadding()
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = appBarState.title,
                            color = MaterialTheme.colors.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    backgroundColor = MaterialTheme.colors.primary,
                    actions = appBarState.actions,
                    elevation = 0.dp
                )
            }

            Scaffold(
                contentWindowInsets = WindowInsets(0, 0, 0, 0)
            ) {
                NavHost(
                    modifier = modifier.padding(it),
                    navController = navController,
                    startDestination = startDestination
                ) {
                    composable(ROUTE_HOME) {
                        TrainingScreen(
                            viewModel = hiltViewModel(),
                            hostAppBar = { state -> appBarState = state },
                            navigateToActivity = { id -> navController.navigate(buildActivityRoute(id)) },
                            navigateToProfile = { navController.navigate(ROUTE_PROFILE) }
                        )
                    }
                    composable(
                        route = "$ROUTE_ACTIVITY/{$ROUTE_ACTIVITY_ID}",
                        arguments = listOf(navArgument(ROUTE_ACTIVITY_ID) { type = NavType.LongType })
                    ) {
                        ActivityScreen(
                            viewModel = hiltViewModel(),
                            hostAppBar = { state -> appBarState = state },
                            openLink = openLink
                        )
                    }
                    composable(ROUTE_PROFILE) {
                        ProfileScreen(
                            viewModel = hiltViewModel(),
                            hostAppBar = { state -> appBarState = state },
                            onSignOutClick = signOut
                        )
                    }
                }
            }
        }
    }
}

data class AppBarState(
    val title: String = "",
    val actions: @Composable RowScope.() -> Unit = {}
)

const val ROUTE_HOME = "home"
const val ROUTE_PROFILE = "profile"
const val ROUTE_ACTIVITY = "activity"
const val ROUTE_ACTIVITY_ID = "id"

private fun buildActivityRoute(id: Long) = "$ROUTE_ACTIVITY/$id"
