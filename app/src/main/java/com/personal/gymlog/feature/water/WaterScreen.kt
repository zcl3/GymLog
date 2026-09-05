package com.personal.gymlog.feature.water

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
import com.personal.gymlog.data.local.entity.WaterEntry
import com.personal.gymlog.data.repository.GymLogRepository
import com.personal.gymlog.data.settings.SettingsRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun WaterScreen(repository: GymLogRepository, settingsRepository: SettingsRepository) {
    val entries by repository.observeWater().collectAsStateWithLifecycle(emptyList())
    val scope = rememberCoroutineScope(); var custom by remember { mutableStateOf(false) }
    val settings by settingsRepository.settings.collectAsStateWithLifecycle(com.personal.gymlog.data.settings.AppSettings()); val total = entries.sumOf { it.amountMl }; val goal = settings.dailyWaterGoalMl; val percent = if (goal == 0) 0 else total * 100 / goal
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("喝水"); Text("$total / $goal ml"); Text("$percent%")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf(100, 250, 500).forEach { amount -> Button(onClick = { scope.launch { repository.addWater(amount) } }) { Text("+$amount ml") } } }
        Button(onClick = { custom = true }, Modifier.fillMaxWidth()) { Text("自定义饮水量") }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) { items(entries, key = { it.id }) { WaterRow(it) { scope.launch { repository.deleteWater(it.id) } } } }
    }
    if (custom) CustomWaterDialog({ custom = false }) { amount -> scope.launch { repository.addWater(amount); custom = false } }
}

@Composable private fun WaterRow(entry: WaterEntry, onDelete: () -> Unit) { Card(Modifier.fillMaxWidth()) { Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) { Text("${entry.amountMl} ml"); TextButton(onClick = onDelete) { Text("删除") } } } }

@Composable private fun CustomWaterDialog(onDismiss: () -> Unit, onSave: (Int) -> Unit) { var amount by remember { mutableStateOf("") }; AlertDialog(onDismissRequest = onDismiss, title = { Text("自定义饮水量") }, text = { OutlinedTextField(amount, { amount = it }, label = { Text("毫升") }, singleLine = true) }, confirmButton = { Button(enabled = (amount.toIntOrNull() ?: 0) > 0, onClick = { onSave(amount.toInt()) }) { Text("保存") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("取消") } }) }
