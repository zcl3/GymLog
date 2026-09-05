package com.personal.gymlog.feature.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.personal.gymlog.data.local.entity.Exercise
import com.personal.gymlog.data.local.entity.SetRecord
import com.personal.gymlog.data.local.entity.WorkoutExercise
import com.personal.gymlog.data.local.entity.WorkoutSession
import com.personal.gymlog.data.repository.GymLogRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun WorkoutScreen(repository: GymLogRepository) {
    val scope = rememberCoroutineScope()
    var session by remember { mutableStateOf<WorkoutSession?>(null) }
    var exercises by remember { mutableStateOf<List<WorkoutExercise>>(emptyList()) }
    var available by remember { mutableStateOf<List<Exercise>>(emptyList()) }
    var showAdd by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { session = repository.inProgress(); available = repository.allExercises(); session?.let { exercises = repository.exercises(it.id) } }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("训练", style = MaterialTheme.typography.headlineLarge)
        if (session == null) {
            Text("开始一次新的训练，记录每个动作和组数。", color = MaterialTheme.colorScheme.onSurfaceVariant)
            Button(onClick = { scope.launch { val id = repository.startWorkout("我的训练"); session = repository.inProgress(); exercises = repository.exercises(id) } }) { Text("开始新训练") }
        } else {
            Text(session!!.name, style = MaterialTheme.typography.titleLarge)
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(exercises, key = { it.id }) { exercise -> ExerciseCard(repository, exercise) }
                item { OutlinedButton(onClick = { showAdd = true }, Modifier.fillMaxWidth()) { Text("添加动作") } }
                item { Button(onClick = { scope.launch { repository.completeWorkout(session!!.id); session = null; exercises = emptyList() } }, Modifier.fillMaxWidth()) { Text("完成训练") } }
            }
        }
    }
    if (showAdd && session != null) AddExerciseDialog(available, { showAdd = false }, { exercise -> scope.launch { repository.addWorkoutExercise(session!!.id, exercise, exercises.size); exercises = repository.exercises(session!!.id); showAdd = false } })
}

@Composable
private fun ExerciseCard(repository: GymLogRepository, exercise: WorkoutExercise) {
    val scope = rememberCoroutineScope()
    var sets by remember { mutableStateOf<List<SetRecord>>(emptyList()) }
    LaunchedEffect(exercise.id) { sets = repository.sets(exercise.id) }
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(exercise.nameSnapshot, style = MaterialTheme.typography.titleMedium)
            sets.forEach { set -> SetRow(set) { weight, reps, completed -> scope.launch { repository.updateSet(set, weight, reps, completed); sets = repository.sets(exercise.id) } } }
            OutlinedButton(onClick = { scope.launch { repository.addSet(exercise.id, sets.size); sets = repository.sets(exercise.id) } }) { Text("添加一组") }
        }
    }
}

@Composable
private fun SetRow(set: SetRecord, onChange: (Int, Int, Boolean) -> Unit) {
    var weight by remember(set.id, set.weightGrams) { mutableStateOf(if (set.weightGrams == 0) "" else (set.weightGrams / 1000.0).toString()) }
    var reps by remember(set.id, set.reps) { mutableStateOf(if (set.reps == 0) "" else set.reps.toString()) }
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("第${set.position + 1}组", Modifier.padding(top = 16.dp))
        OutlinedTextField(weight, { weight = it }, Modifier.weight(1f), label = { Text("kg") }, singleLine = true)
        OutlinedTextField(reps, { reps = it }, Modifier.weight(1f), label = { Text("次数") }, singleLine = true)
        Checkbox(set.isCompleted, { onChange(((weight.toDoubleOrNull() ?: 0.0) * 1000).toInt(), reps.toIntOrNull() ?: 0, it) })
    }
}

@Composable
private fun AddExerciseDialog(exercises: List<Exercise>, onDismiss: () -> Unit, onPick: (Exercise) -> Unit) {
    AlertDialog(onDismissRequest = onDismiss, title = { Text("选择动作") }, text = { LazyColumn { items(exercises) { exercise -> TextButton(onClick = { onPick(exercise) }, Modifier.fillMaxWidth()) { Text("${exercise.name} · ${exercise.bodyPart}") } } } }, confirmButton = { TextButton(onClick = onDismiss) { Text("取消") } })
}
