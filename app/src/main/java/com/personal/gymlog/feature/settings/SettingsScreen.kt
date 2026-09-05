package com.personal.gymlog.feature.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.gymlog.data.settings.SettingsRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun SettingsScreen(repository: SettingsRepository) {
    val settings by repository.settings.collectAsStateWithLifecycle(com.personal.gymlog.data.settings.AppSettings())
    val scope = rememberCoroutineScope(); var goal by remember(settings.dailyWaterGoalMl) { mutableStateOf(settings.dailyWaterGoalMl.toString()) }
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("设置"); Text("重量单位：${settings.unit}"); Text("默认休息：${settings.defaultRestSeconds} 秒")
        OutlinedTextField(goal, { goal = it }, label = { Text("每日饮水目标 ml") }, singleLine = true)
        Button(onClick = { scope.launch { repository.setWaterGoalMl(goal.toIntOrNull() ?: settings.dailyWaterGoalMl) } }) { Text("保存设置") }
    }
}
