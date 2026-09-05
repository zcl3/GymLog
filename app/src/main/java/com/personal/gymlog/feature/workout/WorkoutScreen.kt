package com.personal.gymlog.feature.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun WorkoutScreen(navController: NavController) {
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("训练", style = MaterialTheme.typography.headlineLarge)
        Text("开始一次新的训练，或继续未完成的记录。", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Button(onClick = { /* Phase 4: create session */ }) { Text("开始新训练") }
        Text("暂无进行中的训练", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
