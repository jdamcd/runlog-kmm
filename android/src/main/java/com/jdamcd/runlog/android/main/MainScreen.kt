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
    startDestination: String = "home",
    onOpenLink: (String) -> Unit,
    onSignOut: () -> Unit
) {
    RunLogTheme {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = startDestination
        ) {
            composable("home") {
                TrainingScreen(
                    viewModel = hiltViewModel(),
                    onNavigateToActivity = { id -> navController.navigate("activity/$id") },
                    onNavigateToProfile = { navController.navigate("profile") }
                )
            }
            composable(
                route = "activity/{id}",
                arguments = listOf(navArgument("id") { type = NavType.LongType })
            ) { backstackEntry ->
                ActivityScreen(
                    id = backstackEntry.arguments!!.getLong("id"),
                    viewModel = hiltViewModel(),
                    onOpenLink = onOpenLink
                )
            }
            composable("profile") {
                ProfileScreen(
                    viewModel = hiltViewModel(),
                    onSignOutClick = onSignOut
                )
            }
        }
    }
}
