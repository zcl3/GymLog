package com.personal.gymlog.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.personal.gymlog.navigation.AppDestination

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("今天", style = MaterialTheme.typography.headlineLarge)
        Text("准备好记录今天的训练了吗？", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("今日训练", style = MaterialTheme.typography.titleLarge)
                Text("还没有训练记录", color = MaterialTheme.colorScheme.onSurfaceVariant)
                Button(onClick = { navController.navigate(AppDestination.Workout.route) }) { Text("开始训练") }
            }
        }
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("今日饮水", style = MaterialTheme.typography.titleLarge)
                Text("0 / 2500 ml")
                Button(onClick = { navController.navigate(AppDestination.Water.route) }) { Text("+250 ml") }
            }
        }
        Button(onClick = { navController.navigate(AppDestination.Nutrition.route) }, modifier = Modifier.fillMaxWidth()) {
            Text("记录饮食")
        }
    }
}
