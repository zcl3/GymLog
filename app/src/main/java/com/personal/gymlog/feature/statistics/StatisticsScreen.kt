package com.personal.gymlog.feature.statistics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.gymlog.data.repository.GymLogRepository

@Composable
fun StatisticsScreen(repository: GymLogRepository) {
    val sessions by repository.observeHistory().collectAsStateWithLifecycle(emptyList())
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("统计", style = MaterialTheme.typography.headlineLarge)
        Text("总训练次数：${sessions.size}")
        Text("最近训练：${sessions.firstOrNull()?.trainingDate ?: "暂无"}")
        Text("更多重量和容量趋势将在后续版本完善", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
