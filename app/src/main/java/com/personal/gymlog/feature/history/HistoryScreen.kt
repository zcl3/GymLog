package com.personal.gymlog.feature.history

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.gymlog.data.local.relation.WorkoutDetails
import com.personal.gymlog.data.repository.GymLogRepository
import com.personal.gymlog.feature.home.trimNumber
import com.personal.gymlog.feature.workout.workoutStartTime

@Composable
fun HistoryScreen(repository: GymLogRepository) {
    val workouts by repository.observeHistoryDetails().collectAsStateWithLifecycle(emptyList())
    var selectedId by remember { mutableStateOf<Long?>(null) }
    val selected = workouts.firstOrNull { it.session.id == selectedId }
    if (selected != null) {
        BackHandler { selectedId = null }
        WorkoutHistoryDetail(selected, onBack = { selectedId = null })
    } else {
        Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("训练历史", style = MaterialTheme.typography.headlineLarge)
            Text("点击任意一次训练查看动作、重量和次数", color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (workouts.isEmpty()) Text("还没有已完成的训练", color = MaterialTheme.colorScheme.onSurfaceVariant)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(workouts, key = { it.session.id }) { workout ->
                    Card(Modifier.fillMaxWidth().clickable { selectedId = workout.session.id }) {
                        Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("${workout.session.trainingDate} · ${workoutStartTime(workout.session)}", style = MaterialTheme.typography.titleMedium)
                            Text("${workout.exercises.size} 个动作 · ${workout.exercises.sumOf { it.sets.count { set -> set.isCompleted } }} 组已完成")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun WorkoutHistoryDetail(workout: WorkoutDetails, onBack: () -> Unit) {
    LazyColumn(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            TextButton(onClick = onBack) { Text("返回训练历史") }
            Text("${workout.session.trainingDate} · ${workoutStartTime(workout.session)}", style = MaterialTheme.typography.headlineLarge)
            Text("训练明细", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        items(workout.exercises, key = { it.exercise.id }) { exercise ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(exercise.exercise.nameSnapshot, style = MaterialTheme.typography.titleMedium)
                    val completed = exercise.sets.filter { it.isCompleted }
                    if (completed.isEmpty()) Text("没有完成的组")
                    completed.forEach { set ->
                        Text("第 ${set.position + 1} 组 · ${trimNumber(set.weightGrams / 1000.0)} kg × ${set.reps}")
                    }
                }
            }
        }
    }
}
