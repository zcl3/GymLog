package com.personal.gymlog.navigation

import androidx.navigation.NavController

/** Keeps the dashboard as the single root of the bottom navigation stack. */
fun NavController.navigateTopLevel(destination: AppDestination) {
    if (destination == AppDestination.Home) {
        if (!popBackStack(AppDestination.Home.route, inclusive = false)) {
            navigate(AppDestination.Home.route) { launchSingleTop = true }
        }
        return
    }
    navigate(destination.route) {
        popUpTo(AppDestination.Home.route) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
