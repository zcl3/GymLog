package com.personal.gymlog.feature.nutrition

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
fun NutritionScreen(navController: NavController) {
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("饮食", style = MaterialTheme.typography.headlineLarge)
        Text("早餐 · 午餐 · 晚餐 · 加餐", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("今天还没有饮食记录")
        Button(onClick = { /* Phase 8: add food */ }) { Text("添加食物") }
    }
}
