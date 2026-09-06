package com.personal.gymlog.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.personal.gymlog.navigation.bottomDestinations
import com.personal.gymlog.navigation.navigateTopLevel

@Composable
fun GymLogBottomBar(navController: NavHostController) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination
    NavigationBar {
        bottomDestinations.forEach { item ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.route == item.destination.route } == true,
                onClick = {
                    navController.navigateTopLevel(item.destination)
                },
                icon = { Icon(item.icon, contentDescription = item.destination.label) },
                label = { Text(item.destination.label) },
            )
        }
    }
}
