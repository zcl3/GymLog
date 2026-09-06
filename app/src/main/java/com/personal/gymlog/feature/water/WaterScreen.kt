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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.personal.gymlog.data.local.entity.WaterEntry
import com.personal.gymlog.data.repository.GymLogRepository
import com.personal.gymlog.data.settings.AppSettings
import com.personal.gymlog.data.settings.SettingsRepository
import com.personal.gymlog.data.settings.recordDate
import com.personal.gymlog.navigation.AppDestination
import com.personal.gymlog.navigation.navigateTopLevel
import kotlinx.coroutines.launch

@Composable
fun WaterScreen(repository: GymLogRepository, settingsRepository: SettingsRepository, navController: NavController) {
    val settings by settingsRepository.settings.collectAsStateWithLifecycle(AppSettings())
    val date = settings.recordDate()
    val entries by repository.observeWater(date).collectAsStateWithLifecycle(emptyList())
    val scope = rememberCoroutineScope()
    var custom by remember { mutableStateOf(false) }
    val total = entries.sumOf { it.amountMl }
    val goal = settings.dailyWaterGoalMl
    val progress = (total.toFloat() / goal).coerceIn(0f, 1f)
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("喝水")
        Text("$date · $total / $goal ml")
        LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth())
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(100, 250, 500).forEach { amount ->
                OutlinedButton(onClick = { scope.launch { repository.addWater(amount, date) } }) { Text("+$amount") }
            }
        }
        Button(onClick = { custom = true }, Modifier.fillMaxWidth()) { Text("自定义饮水量") }
        TextButton(onClick = { navController.navigateTopLevel(AppDestination.More) }) { Text("调整每日饮水目标") }
        if (entries.isEmpty()) Text("当天还没有饮水记录")
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(entries, key = { it.id }) { entry -> WaterRow(entry) { scope.launch { repository.deleteWater(entry.id) } } }
        }
    }
    if (custom) CustomWaterDialog(onDismiss = { custom = false }) { amount ->
        scope.launch { repository.addWater(amount, date); custom = false }
    }
}

@Composable
private fun WaterRow(entry: WaterEntry, onDelete: () -> Unit) {
    Card(Modifier.fillMaxWidth()) {
        Row(Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("${entry.amountMl} ml")
            TextButton(onClick = onDelete) { Text("删除") }
        }
    }
}

@Composable
private fun CustomWaterDialog(onDismiss: () -> Unit, onSave: (Int) -> Unit) {
    var amount by remember { mutableStateOf("") }
    val value = amount.toIntOrNull()
    val valid = value != null && value in 1..10_000
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("自定义饮水量") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(amount, { amount = it }, label = { Text("毫升") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                if (amount.isNotBlank() && !valid) Text("请输入 1–10000 ml")
            }
        },
        confirmButton = { Button(enabled = valid, onClick = { onSave(value!!) }) { Text("保存") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("取消") } },
    )
}
