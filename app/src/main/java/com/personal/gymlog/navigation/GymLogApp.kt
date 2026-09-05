package com.personal.gymlog.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.personal.gymlog.feature.home.HomeScreen
import com.personal.gymlog.feature.more.MoreScreen
import com.personal.gymlog.feature.nutrition.NutritionScreen
import com.personal.gymlog.feature.water.WaterScreen
import com.personal.gymlog.feature.workout.WorkoutScreen
import com.personal.gymlog.ui.components.GymLogBottomBar

@Composable
fun GymLogApp() {
    val navController = rememberNavController()
    Scaffold(bottomBar = { GymLogBottomBar(navController) }) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = AppDestination.Home.route,
            modifier = Modifier.padding(paddingValues),
        ) {
            composable(AppDestination.Home.route) { HomeScreen(navController) }
            composable(AppDestination.Workout.route) { WorkoutScreen(navController) }
            composable(AppDestination.Nutrition.route) { NutritionScreen(navController) }
            composable(AppDestination.Water.route) { WaterScreen(navController) }
            composable(AppDestination.More.route) { MoreScreen(navController) }
            listOf(AppDestination.Statistics, AppDestination.Exercises, AppDestination.Templates, AppDestination.Settings)
                .forEach { destination -> composable(destination.route) { PlaceholderScreen(destination.label) } }
        }
    }
}
