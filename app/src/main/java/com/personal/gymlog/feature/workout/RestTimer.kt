package com.personal.gymlog.feature.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

fun formatRestTime(seconds: Long): String {
    val safe = seconds.coerceAtLeast(0)
    return "%02d:%02d".format(safe / 60, safe % 60)
}

@Composable
fun RestTimer(defaultSeconds: Long = 90) {
    var remaining by remember { mutableLongStateOf(defaultSeconds) }
    var running by remember { mutableStateOf(false) }
    LaunchedEffect(running) {
        while (running && remaining > 0) {
            delay(1_000)
            remaining -= 1
        }
        if (remaining == 0L) running = false
    }
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("休息 ${formatRestTime(remaining)}")
        Button(onClick = { running = !running; if (remaining == 0L) remaining = defaultSeconds }) { Text(if (running) "暂停" else "开始") }
        OutlinedButton(onClick = { remaining = defaultSeconds; running = false }) { Text("重置") }
        OutlinedButton(onClick = { remaining += 30 }) { Text("+30秒") }
    }
}
