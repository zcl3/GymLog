package com.personal.gymlog.feature.more

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.personal.gymlog.data.repository.GymLogRepository
import com.personal.gymlog.navigation.AppDestination
import java.time.LocalDate
import kotlinx.coroutines.flow.first

@Composable
fun MoreScreen(navController: NavController, repository: GymLogRepository) {
    val today = LocalDate.now(); var selected by remember { mutableStateOf(today) }; var trainingCount by remember { mutableStateOf(0) }; var foodCount by remember { mutableStateOf(0) }
    LaunchedEffect(selected) { trainingCount = repository.observeWorkouts(selected.toString()).first().size; foodCount = repository.observeFoods(selected.toString()).first().size }
    val days = (1..today.lengthOfMonth()).map { today.withDayOfMonth(it) }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text("更多", style = MaterialTheme.typography.headlineLarge); Text("${today.year}年${today.monthValue}月", style = MaterialTheme.typography.titleLarge)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp)) { items(days) { day -> OutlinedButton(onClick = { selected = day }, Modifier.fillMaxWidth()) { Text(if (day == selected) "✓ $day" else day.toString()) } } }
        Text("${selected}：训练 ${trainingCount} 次 · 饮食 ${foodCount} 项")
        listOf(AppDestination.Statistics, AppDestination.Exercises, AppDestination.Templates, AppDestination.Settings).forEach { destination -> OutlinedButton(onClick = { navController.navigate(destination.route) }, Modifier.fillMaxWidth()) { Text(destination.label) } }
    }
}
