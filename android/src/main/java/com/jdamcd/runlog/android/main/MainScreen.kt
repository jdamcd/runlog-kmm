package com.jdamcd.runlog.android.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = startDestination
        ) {
            composable(ROUTE_HOME) {
                TrainingScreen(
                    viewModel = hiltViewModel(),
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
                    openLink = openLink
                )
            }
            composable(ROUTE_PROFILE) {
                ProfileScreen(
                    viewModel = hiltViewModel(),
                    onSignOutClick = signOut
                )
            }
        }
    }
}

const val ROUTE_HOME = "home"
const val ROUTE_PROFILE = "profile"
const val ROUTE_ACTIVITY = "activity"
const val ROUTE_ACTIVITY_ID = "id"

private fun buildActivityRoute(id: Long) = "$ROUTE_ACTIVITY/$id"
