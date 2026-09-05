package com.personal.gymlog.feature.water

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
fun WaterScreen(navController: NavController) {
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("喝水", style = MaterialTheme.typography.headlineLarge)
        Text("0 / 2500 ml", style = MaterialTheme.typography.headlineMedium)
        Text("0%", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Button(onClick = { /* Phase 9: save water entry */ }) { Text("+250 ml") }
        Text("今天还没有饮水记录", color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
