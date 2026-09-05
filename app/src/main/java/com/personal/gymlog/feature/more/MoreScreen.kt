package com.personal.gymlog.feature.more

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.personal.gymlog.navigation.AppDestination

@Composable
fun MoreScreen(navController: NavController) {
    val entries = listOf(
        AppDestination.Statistics,
        AppDestination.Exercises,
        AppDestination.Templates,
        AppDestination.Settings,
    )
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("更多")
        entries.forEach { destination ->
            OutlinedButton(
                onClick = { navController.navigate(destination.route) },
                modifier = Modifier.fillMaxWidth(),
            ) { Text(destination.label) }
        }
    }
}
