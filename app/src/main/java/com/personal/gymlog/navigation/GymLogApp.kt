package com.personal.gymlog.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Density
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import com.personal.gymlog.GymLogApplication
import com.personal.gymlog.feature.exercise.ExerciseLibraryScreen
import com.personal.gymlog.feature.home.HomeScreen
import com.personal.gymlog.feature.history.HistoryScreen
import com.personal.gymlog.feature.more.MoreScreen
import com.personal.gymlog.feature.nutrition.NutritionScreen
import com.personal.gymlog.feature.template.TemplateScreen
import com.personal.gymlog.feature.statistics.StatisticsScreen
import com.personal.gymlog.feature.settings.SettingsScreen
import com.personal.gymlog.data.settings.SettingsRepository
import com.personal.gymlog.feature.water.WaterScreen
import com.personal.gymlog.feature.workout.WorkoutScreen
import com.personal.gymlog.ui.components.GymLogBottomBar

@Composable
fun GymLogApp() {
    val navController = rememberNavController()
    val context = LocalContext.current.applicationContext
    val repository = (context as GymLogApplication).repository
    val settingsRepository = remember(context) { SettingsRepository(context) }
    val settings by settingsRepository.settings.collectAsStateWithLifecycle(com.personal.gymlog.data.settings.AppSettings())
    val density = LocalDensity.current
    CompositionLocalProvider(LocalDensity provides Density(density.density, density.fontScale * settings.fontScale)) {
    Scaffold(bottomBar = { GymLogBottomBar(navController) }) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = AppDestination.Home.route,
            modifier = Modifier.padding(paddingValues),
        ) {
            composable(AppDestination.Home.route) { HomeScreen(navController, repository, settingsRepository) }
            composable(AppDestination.Workout.route) { WorkoutScreen(repository, settingsRepository) }
            composable(AppDestination.History.route) { HistoryScreen(repository) }
            composable(AppDestination.Nutrition.route) { NutritionScreen(repository, settingsRepository) }
            composable(AppDestination.Water.route) { WaterScreen(repository, settingsRepository, navController) }
            composable(AppDestination.More.route) { MoreScreen(navController, repository, settingsRepository) }
            composable(AppDestination.Exercises.route) { ExerciseLibraryScreen(repository) }
            composable(AppDestination.Templates.route) { TemplateScreen(repository) }
            composable(AppDestination.Statistics.route) { StatisticsScreen(repository) }
            composable(AppDestination.Settings.route) { SettingsScreen(settingsRepository) }
        }
    }
    }
}
