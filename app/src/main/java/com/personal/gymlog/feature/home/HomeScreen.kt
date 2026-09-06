package com.personal.gymlog.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.personal.gymlog.data.repository.GymLogRepository
import com.personal.gymlog.data.settings.AppSettings
import com.personal.gymlog.data.settings.SettingsRepository
import com.personal.gymlog.data.settings.recordDate
import com.personal.gymlog.feature.workout.workoutSummary
import com.personal.gymlog.navigation.AppDestination
import com.personal.gymlog.navigation.navigateTopLevel
import java.time.LocalDate

@Composable
fun HomeScreen(navController: NavController, repository: GymLogRepository, settingsRepository: SettingsRepository) {
    val settings by settingsRepository.settings.collectAsStateWithLifecycle(AppSettings())
    val date = settings.recordDate()
    val workoutDetails by repository.observeWorkoutDetails(date).collectAsStateWithLifecycle(emptyList())
    val foods by repository.observeFoods(date).collectAsStateWithLifecycle(emptyList())
    val water by repository.observeWater(date).collectAsStateWithLifecycle(emptyList())
    val dateTitle = if (date == LocalDate.now().toString()) "今天" else date
    val totalWater = water.sumOf { it.amountMl }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(dateTitle, style = MaterialTheme.typography.headlineLarge)
                    Text("当天全部记录", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                IconButton(onClick = { navController.navigate(AppDestination.More.route) }) {
                    Icon(Icons.Outlined.MoreHoriz, contentDescription = "更多设置")
                }
            }
        }
        item {
            val workoutText = if (workoutDetails.isEmpty()) "还没有训练记录" else workoutDetails.joinToString("\n\n", transform = ::workoutSummary)
            RecordCard("训练", workoutText) { navController.navigateTopLevel(AppDestination.Workout) }
        }
        item {
            val foodText = if (foods.isEmpty()) "还没有饮食记录" else foods.joinToString("\n") { entry ->
                buildString {
                    append("${entry.mealType} · ${entry.name}")
                    entry.proteinGrams?.let { append(" · 蛋白质 ${trimNumber(it)} g") }
                    entry.caloriesKcal?.let { append(" · ${trimNumber(it)} kcal") }
                }
            }
            val nutritionSummary = "今日合计：热量 ${trimNumber(foods.sumOf { it.caloriesKcal ?: 0.0 })} kcal · 蛋白质 ${trimNumber(foods.sumOf { it.proteinGrams ?: 0.0 })} g · 碳水 ${trimNumber(foods.sumOf { it.carbsGrams ?: 0.0 })} g · 脂肪 ${trimNumber(foods.sumOf { it.fatGrams ?: 0.0 })} g"
            RecordCard("饮食", "$foodText\n$nutritionSummary") { navController.navigateTopLevel(AppDestination.Nutrition) }
        }
        item {
            val waterText = if (water.isEmpty()) "还没有饮水记录" else water.joinToString("\n") { "${it.amountMl} ml" }
            RecordCard("饮水", "$totalWater / ${settings.dailyWaterGoalMl} ml\n$waterText") { navController.navigateTopLevel(AppDestination.Water) }
        }
    }
}

@Composable
private fun RecordCard(title: String, content: String, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge)
            Text(content)
            Button(onClick = onClick) { Text("查看并记录") }
        }
    }
}

fun trimNumber(value: Double): String = if (value % 1.0 == 0.0) value.toInt().toString() else "%.1f".format(value)
