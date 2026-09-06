package com.personal.gymlog.feature.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.gymlog.data.repository.GymLogRepository
import com.personal.gymlog.feature.home.trimNumber
import com.personal.gymlog.feature.workout.workoutVolumeKg

@Composable
fun StatisticsScreen(repository: GymLogRepository) {
    val workouts by repository.observeHistoryDetails().collectAsStateWithLifecycle(emptyList())
    val completedSets = workouts.flatMap { it.exercises }.flatMap { it.sets }.filter { it.isCompleted }
    val actionCount = workouts.sumOf { it.exercises.size }
    val volume = completedSets.sumOf { workoutVolumeKg(it.weightGrams, it.reps) }

    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Text("统计", style = MaterialTheme.typography.headlineLarge)
            Text("仅统计已完成的训练和已完成的组", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        item { StatisticCard("完成训练", "${workouts.size} 次") }
        item { StatisticCard("完成动作", "$actionCount 个") }
        item { StatisticCard("完成训练组", "${completedSets.size} 组") }
        item { StatisticCard("累计训练量", "${trimNumber(volume)} kg") }
        item { StatisticCard("最近训练", workouts.firstOrNull()?.session?.trainingDate ?: "暂无") }
    }
}

@Composable
private fun StatisticCard(title: String, value: String) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(value, style = MaterialTheme.typography.headlineSmall)
        }
    }
}
