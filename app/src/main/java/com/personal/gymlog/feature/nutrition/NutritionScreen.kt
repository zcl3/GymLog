package com.personal.gymlog.feature.nutrition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personal.gymlog.data.local.entity.FoodEntry
import com.personal.gymlog.data.repository.GymLogRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun NutritionScreen(repository: GymLogRepository) {
    val foods by repository.observeFoods().collectAsStateWithLifecycle(emptyList())
    val scope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("饮食")
        Button(onClick = { showDialog = true }, Modifier.fillMaxWidth()) { Text("添加食物") }
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) { items(foods, key = { it.id }) { FoodRow(it) { scope.launch { repository.deleteFood(it.id) } } } }
    }
    if (showDialog) FoodDialog({ showDialog = false }) { name, meal, kcal, protein -> scope.launch { repository.addFood(name, meal, kcal, protein, null, null); showDialog = false } }
}

@Composable private fun FoodRow(food: FoodEntry, onDelete: (FoodEntry) -> Unit) {
    Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(14.dp)) { Text("${food.mealType} · ${food.name}"); food.caloriesKcal?.let { Text("${it} kcal") }; TextButton(onClick = { onDelete(food) }) { Text("删除") } } }
}

@Composable private fun FoodDialog(onDismiss: () -> Unit, onSave: (String, String, Double?, Double?) -> Unit) {
    var name by remember { mutableStateOf("") }; var meal by remember { mutableStateOf("早餐") }; var kcal by remember { mutableStateOf("") }; var protein by remember { mutableStateOf("") }
    AlertDialog(onDismissRequest = onDismiss, title = { Text("添加食物") }, text = { Column(verticalArrangement = Arrangement.spacedBy(8.dp)) { OutlinedTextField(name, { name = it }, label = { Text("食物名称") }, singleLine = true); OutlinedTextField(meal, { meal = it }, label = { Text("餐次") }, singleLine = true); OutlinedTextField(kcal, { kcal = it }, label = { Text("热量 kcal（可选）") }, singleLine = true); OutlinedTextField(protein, { protein = it }, label = { Text("蛋白质 g（可选）") }, singleLine = true) } }, confirmButton = { Button(enabled = name.isNotBlank(), onClick = { onSave(name, meal, kcal.toDoubleOrNull(), protein.toDoubleOrNull()) }) { Text("保存") } }, dismissButton = { TextButton(onClick = onDismiss) { Text("取消") } })
}
