package com.jdamcd.runlog.android.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jdamcd.runlog.android.profile.ProfileScreen
import com.jdamcd.runlog.android.training.TrainingScreen
import com.jdamcd.runlog.android.ui.RunLogTheme

const val NAV_ACTIVITIES = "activities"
const val NAV_PROFILE = "profile"

@Composable
fun MainNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NAV_ACTIVITIES,
    onOpenLink: (String) -> Unit,
    onSignOut: () -> Unit
) {
    RunLogTheme {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = startDestination
        ) {
            composable(NAV_ACTIVITIES) {
                TrainingScreen(
                    viewModel = hiltViewModel(),
                    onOpenLink = onOpenLink,
                    onNavigateToProfile = { navController.navigate(NAV_PROFILE) }
                )
            }
            composable(NAV_PROFILE) {
                ProfileScreen(
                    viewModel = hiltViewModel(),
                    onSignOutClick = onSignOut
                )
            }
        }
    }
}
