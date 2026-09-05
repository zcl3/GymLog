package com.personal.gymlog.feature.exercise

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.personal.gymlog.data.local.entity.Exercise
import com.personal.gymlog.data.repository.GymLogRepository

private val bodyParts = listOf("胸", "背", "腿", "肩", "二头", "三头", "核心", "其他")

@Composable
fun ExerciseLibraryScreen(repository: GymLogRepository) {
    val model: ExerciseLibraryViewModel = viewModel(factory = ExerciseLibraryViewModel.Factory(repository))
    val state by model.state.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("动作库", style = MaterialTheme.typography.headlineLarge)
        OutlinedTextField(state.query, model::setQuery, Modifier.fillMaxWidth(), label = { Text("搜索动作") }, singleLine = true)
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(state.bodyPart == null, { model.setBodyPart(null) }, label = { Text("全部") })
                    bodyParts.forEach { part -> FilterChip(state.bodyPart == part, { model.setBodyPart(if (state.bodyPart == part) null else part) }, label = { Text(part) }) }
                }
            }
            items(state.exercises, key = { it.id }) { exercise -> ExerciseRow(exercise, model::archive) }
            item { Button(onClick = { showDialog = true }, Modifier.fillMaxWidth()) { Text("新建自定义动作") } }
        }
    }
    if (showDialog) AddExerciseDialog({ showDialog = false }, model::add)
}

@Composable
private fun ExerciseRow(exercise: Exercise, onArchive: (Exercise) -> Unit) {
    Card(Modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column { Text(exercise.name, style = MaterialTheme.typography.titleMedium); Text(exercise.bodyPart, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            if (!exercise.isBuiltIn) TextButton(onClick = { onArchive(exercise) }) { Text("删除") }
        }
    }
}

@Composable
private fun AddExerciseDialog(onDismiss: () -> Unit, onAdd: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var part by remember { mutableStateOf(bodyParts.first()) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("新建动作") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(name, { name = it }, label = { Text("动作名称") }, singleLine = true)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) { bodyParts.take(4).forEach { AssistChip(onClick = { part = it }, label = { Text(if (part == it) "✓ $it" else it) }) } }
            }
        },
        confirmButton = { Button(enabled = name.trim().isNotEmpty(), onClick = { onAdd(name, part); onDismiss() }) { Text("保存") } },
        dismissButton = { OutlinedButton(onClick = onDismiss) { Text("取消") } },
    )
}
