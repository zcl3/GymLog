package com.personal.gymlog.feature.more

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.personal.gymlog.data.repository.GymLogRepository
import com.personal.gymlog.data.settings.AppSettings
import com.personal.gymlog.data.settings.SettingsRepository
import com.personal.gymlog.data.settings.recordDate
import com.personal.gymlog.navigation.AppDestination
import java.time.LocalDate
import kotlinx.coroutines.launch

@Composable
fun MoreScreen(navController: NavController, repository: GymLogRepository, settingsRepository: SettingsRepository) {
    val settings by settingsRepository.settings.collectAsStateWithLifecycle(AppSettings())
    val date = settings.recordDate()
    val workouts by repository.observeWorkouts(date).collectAsStateWithLifecycle(emptyList())
    val foods by repository.observeFoods(date).collectAsStateWithLifecycle(emptyList())
    val water by repository.observeWater(date).collectAsStateWithLifecycle(emptyList())
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var dialog by remember { mutableStateOf<SettingDialog?>(null) }
    val selected = LocalDate.parse(date)

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp).testTag("more_list"),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { Text("更多", style = MaterialTheme.typography.headlineLarge) }
        item {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("记录日期", style = MaterialTheme.typography.titleMedium)
                    Text("$date。切换日期后，首页、训练、饮食和喝水都会显示该日记录。")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = {
                            DatePickerDialog(context, { _, year, month, day ->
                                scope.launch { settingsRepository.setCurrentDate(LocalDate.of(year, month + 1, day).toString()) }
                            }, selected.year, selected.monthValue - 1, selected.dayOfMonth).show()
                        }) { Text("选择年月日") }
                        TextButton(onClick = { scope.launch { settingsRepository.setCurrentDate(null) } }) { Text("回到今天") }
                    }
                }
            }
        }
        item {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("当天摘要", style = MaterialTheme.typography.titleMedium)
                    Text("训练 ${workouts.size} 次 · 饮食 ${foods.size} 项 · 饮水 ${water.sumOf { it.amountMl }} ml")
                }
            }
        }
        item { Text("应用设置", style = MaterialTheme.typography.titleLarge) }
        item { SettingRow("每日饮水目标", "${settings.dailyWaterGoalMl} ml", "修改") { dialog = SettingDialog.WaterGoal } }
        item { SettingRow("默认组间休息", "${settings.defaultRestSeconds} 秒", "修改") { dialog = SettingDialog.RestSeconds } }
        item {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("字体大小", style = MaterialTheme.typography.titleMedium)
                    Text(if (settings.fontScale == 1f) "标准" else if (settings.fontScale < 1.25f) "大" else "特大")
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("标准" to 1f, "大" to 1.15f, "特大" to 1.3f).forEach { (label, scale) ->
                            OutlinedButton(onClick = { scope.launch { settingsRepository.setFontScale(scale) } }) { Text(label) }
                        }
                    }
                }
            }
        }
        item { Text("数据与工具", style = MaterialTheme.typography.titleLarge) }
        item {
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(8.dp)) {
                    listOf(AppDestination.Statistics, AppDestination.History, AppDestination.Exercises, AppDestination.Templates).forEach { destination ->
                        TextButton(onClick = { navController.navigate(destination.route) }, modifier = Modifier.fillMaxWidth()) { Text(destination.label) }
                    }
                }
            }
        }
    }
    dialog?.let { type ->
        NumberSettingDialog(type, settings, onDismiss = { dialog = null }) { value ->
            scope.launch {
                if (type == SettingDialog.WaterGoal) settingsRepository.setWaterGoalMl(value) else settingsRepository.setDefaultRestSeconds(value)
                dialog = null
            }
        }
    }
}

private enum class SettingDialog { WaterGoal, RestSeconds }

@Composable
private fun SettingRow(title: String, value: String, action: String, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column { Text(title, style = MaterialTheme.typography.titleMedium); Text(value) }
            TextButton(onClick = onClick) { Text(action) }
        }
    }
}

@Composable
private fun NumberSettingDialog(type: SettingDialog, settings: AppSettings, onDismiss: () -> Unit, onSave: (Int) -> Unit) {
    val initial = if (type == SettingDialog.WaterGoal) settings.dailyWaterGoalMl else settings.defaultRestSeconds
    var text by remember(type, initial) { mutableStateOf(initial.toString()) }
    val value = text.toIntOrNull()
    val range = if (type == SettingDialog.WaterGoal) 100..20_000 else 10..3_600
    val title = if (type == SettingDialog.WaterGoal) "每日饮水目标" else "默认组间休息"
    val suffix = if (type == SettingDialog.WaterGoal) "ml" else "秒"
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(text, { text = it }, label = { Text("数值（${range.first}–${range.last} $suffix）") }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                if (text.isNotBlank() && (value == null || value !in range)) Text("请输入 ${range.first}–${range.last} $suffix")
            }
        },
        confirmButton = { Button(enabled = value != null && value in range, onClick = { onSave(value!!) }) { Text("保存") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("取消") } },
    )
}
