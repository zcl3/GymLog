package com.personal.gymlog.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.personal.gymlog.data.repository.GymLogRepository
import com.personal.gymlog.data.settings.SettingsRepository
import com.personal.gymlog.navigation.AppDestination
import java.time.LocalDate
import com.personal.gymlog.data.local.relation.WorkoutDetails

@Composable
fun HomeScreen(navController: NavController, repository: GymLogRepository, settings: SettingsRepository) {
    val date = LocalDate.now().toString(); val workouts by repository.observeWorkouts(date).collectAsStateWithLifecycle(emptyList()); val foods by repository.observeFoods(date).collectAsStateWithLifecycle(emptyList()); val water by repository.observeWater(date).collectAsStateWithLifecycle(emptyList()); val appSettings by settings.settings.collectAsStateWithLifecycle(com.personal.gymlog.data.settings.AppSettings())
    val totalWater = water.sumOf { it.amountMl }; var details by remember { mutableStateOf<List<WorkoutDetails>>(emptyList()) }
    LaunchedEffect(workouts) { details = workouts.mapNotNull { repository.workoutDetails(it.id) } }
    LazyColumn(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item { Text("今天", style = MaterialTheme.typography.headlineLarge) }
        item { Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp)) { Text("今日训练", style = MaterialTheme.typography.titleLarge); Text(if (details.isEmpty()) "还没有训练记录" else details.flatMap { detail -> detail.exercises.flatMap { exercise -> exercise.sets.filter { it.isCompleted }.map { "${exercise.exercise.nameSnapshot}：${it.weightGrams / 1000.0} kg × ${it.reps}" } } }.joinToString("\n")); Button(onClick = { navController.navigate(AppDestination.Workout.route) }) { Text("开始训练") } } } }
        item { Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp)) { Text("今日饮食", style = MaterialTheme.typography.titleLarge); Text(if (foods.isEmpty()) "还没有饮食记录" else "已记录 ${foods.size} 项") ; Button(onClick = { navController.navigate(AppDestination.Nutrition.route) }) { Text("记录饮食") } } } }
        item { Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp)) { Text("今日饮水", style = MaterialTheme.typography.titleLarge); Text("$totalWater / ${appSettings.dailyWaterGoalMl} ml"); Button(onClick = { navController.navigate(AppDestination.Water.route) }) { Text("+250 ml") } } } }
    }
}
